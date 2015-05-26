(ns player-rankings.logic.database
  (:require [clojure.set :refer [difference]]
            [clojure.string :as string]
            [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [clojurewerkz.neocons.rest.transaction :as transaction]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]
            [player-rankings.logic.rankings :as rankings]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(defn- create-tournament-node [tournament]
  (let [node (nodes/create conn tournament)]
    (labels/add conn node "tournament")
    node))

(defn- keys->keywords [coll]
  (into {} (for [[k v] coll] [(keyword k) v])))

(defn- get-existing-players []
  (let [query (str "match (p:player) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))

(defn- create-new-player-nodes [player-names]
  (let [query (str "unwind {names} as name "
                   "create (p:player {name: name, aliases: [name]}) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query {:names player-names})]
    (map keys->keywords data)))

(defn normalize-name [player-name]
  (-> player-name
      string/lower-case
      (string/replace #"\s" "")
      (string/split #"\|")
      last))

(defn get-matching-player [player-name players]
  (some #(when (some (fn [existing-player-name]
                       (= (normalize-name player-name)
                          (normalize-name existing-player-name)))
                     (map normalize-name (:aliases %))) %)
        players))

(defn- create-player-nodes [matches]
  (let [first-players (map :player-one matches)
        second-players (map :player-two matches)
        unique-players (distinct (concat first-players second-players))
        existing-players (get-existing-players)
        new-player-names (filter #(not (get-matching-player % existing-players)) unique-players)
        new-players (create-new-player-nodes new-player-names)]
    (concat existing-players new-players)))

(defn- create-match-graph-data [match player-nodes]
  (let [player1-node (get-matching-player (:player-one match) player-nodes)
        player2-node (get-matching-player (:player-two match) player-nodes)]
    {"score" (:scores match)
     "time" (:time match)
     "player_one" {"id" (:id player1-node) "won" (= 1 (:winner match))}
     "player_two" {"id" (:id player2-node) "won" (= 2 (:winner match))}}))

(defn- create-match-graphs [matches]
  (let [player-nodes (create-player-nodes matches)
        match-graph-data (map #(create-match-graph-data % player-nodes) matches)
        query (str "unwind {records} as record "
                   "match (player_one:player) "
                   "where id(player_one) = record.player_one.id "
                   "match (player_two:player) "
                   "where id(player_two) = record.player_two.id "
                   "create (m:match {score: record.score, time: record.time}) "
                   "create (player_one)-[:played {won: record.player_one.won}]->(m) "
                   "create (player_two)-[:played {won: record.player_two.won}]->(m) "
                   "return id(m) as id")]
    (mapv #(% "id") (cypher/tquery conn query {:records match-graph-data}))))

(defn- raw-match-information []
  (let [query (str "match (player:player)-[played:played]-(game:match) "
                   "return id(player) as player_id, id(played) as played_id, played.won as won "
                   "order by game.time")]
    (vec (cypher/tquery conn query))))

(defn- match-information-by-player []
  (reduce (fn [coll match]
            (let [player-id (match "player_id")
                  match-record {:id (match "played_id")
                                :won (match "won")
                                :player_id (match "player_id")}]
              (if (contains? coll player-id)
                (assoc coll player-id (conj (coll player-id) match-record))
                (assoc coll player-id [match-record]))))
          {} (raw-match-information)))

(defn- flatten-rating-record [record]
  {:start ((juxt :rating :rd :volatility) (:start record))
   :end ((juxt :rating :rd :volatility) (:end record))})

(defn- flattened-ratings [wins]
  (->> wins
       rankings/calculate-partial-ratings
       (map flatten-rating-record)))

(defn- ratings-information-by-player []
  (let [player-matches (match-information-by-player)]
    (reduce-kv
     (fn [coll player-id match-records]
       (let [ratings (flattened-ratings (mapv :won match-records))
             new-match-records (map merge match-records ratings)]
         (assoc coll player-id new-match-records))) {} player-matches)))

(defn- get-match-ratings []
  (-> (ratings-information-by-player) vals flatten vec))

(defn update-player-ratings []
  (let [matches (get-match-ratings)
        query (str "unwind {records} as record "
                   "match (p:player)-[played:played]-(:match) "
                   "where id(p) = record.player_id and id(played) = record.id "
                   "set played += {start_rating: record.start, end_rating: record.end} ")]
    (cypher/tquery conn query {:records matches})))

(defn create-tournament-graph [tournament-data]
  (let [tournament-node (create-tournament-node (:tournament tournament-data))
        match-ids (create-match-graphs (:matches tournament-data))
        query (str "unwind {match_ids} as match_id "
                   "match (m:match) "
                   "where id(m) = match_id "
                   "match (t:tournament) "
                   "where id(t) = {tournament_id} "
                   "create (t)-[:hosted]->(m) ")]
    (cypher/tquery conn query {:match_ids match-ids :tournament_id (:id tournament-node)})))

(defn load-tournaments [tournaments]
  (doseq [tournament tournaments]
    (create-tournament-graph tournament))
  (update-player-ratings))

(defn merge-player-nodes
  ([[a b]] (merge-player-nodes [a b] (get-existing-players)))
  ([[a b] players]
   (let [aid (:id (get-matching-player a players))
         bid (:id (get-matching-player b players))
         query (str "match (a:player), (b:player)-[bp:played]-(bm:match) "
                    "where id(a) = {aid} and id(b) = {bid} "
                    "set a.aliases = a.aliases + b.aliases "
                    "create (a)-[:played {won: bp.won}]->(bm) "
                    "delete bp, b")]
     (cypher/tquery conn query {:aid aid, :bid bid}))))
