(ns player-rankings.logic.database
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]
            [player-rankings.logic.rankings :as rankings]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(defn- create-tournament-node [tournament]
  (let [node (nodes/create conn tournament)]
    (labels/add conn node "tournament")
    node))

(defn- create-match-node [match]
  (let [node (nodes/create conn {:score (:scores match) :time (:time match)})]
    (labels/add conn node "match")
    node))

(defn- get-existing-players []
  (let [query (str "match (p:player) "
                   "return id(p) as id, p.name as name")
        results (cypher/tquery conn query)]
    (zipmap (map #(% "name") results)
            (map #(identity {:name (% "name") :id (% "id")}) results))))

(defn- create-player-node [player-name]
  (let [node (nodes/create conn {:name player-name})]
    (labels/add conn node "player")
    node))

(defn create-player-nodes [matches]
  (let [first-players (map :player-one matches)
        second-players (map :player-two matches)
        unique-players-in-tournament (distinct (concat first-players second-players))
        existing-players (get-existing-players)]
    (reduce (fn [coll player]
              (if (existing-players player)
                (into coll {player (existing-players player)})
                (into coll {player (create-player-node player)})))
              {} unique-players-in-tournament)))

(defn- create-match-graph-data [match player-nodes]
  (let [match-node (create-match-node match)
        player1-node (player-nodes (:player-one match))
        player2-node (player-nodes (:player-two match))]
    {"id" (:id match-node)
     "player_one" {"id" (:id player1-node) "won" (= 1 (:winner match))}
     "player_two" {"id" (:id player2-node) "won" (= 2 (:winner match))}}))

(defn- create-match-graphs [matches]
  (let [player-nodes (create-player-nodes matches)
        match-graph-data (map #(create-match-graph-data % player-nodes) matches)
        query (str "unwind {records} as record "
                   "match (m:match) "
                   "where id(m) = record.id "
                   "match (player_one:player) "
                   "where id(player_one) = record.player_one.id "
                   "match (player_two:player) "
                   "where id(player_two) = record.player_two.id "
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
                  match-record {:id (match "played_id") :won (match "won")}]
              (if (contains? coll player-id)
                (assoc coll player-id (conj (coll player-id) match-record))
                (assoc coll player-id [match-record]))))
          {} (raw-match-information)))

(defn- flatten-rating-record [record]
  (reduce-kv (fn [coll k v]
               (reduce-kv (fn [coll2 k2 v2]
                            (into coll2 {(str (name k) "_" (name k2)) v2}))
                          coll v))
             {} record))

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
                   "match ()-[played:played]-() "
                   "where id(played) = record.id "
                   "set played = record "
                   "remove played.id")]
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
    (cypher/tquery conn query {:match_ids match-ids :tournament_id (:id tournament-node)})
    (update-player-ratings)))
