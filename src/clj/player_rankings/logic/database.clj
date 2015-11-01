(ns player-rankings.logic.database
  (:require [clojure.set :refer [difference intersection]]
            [clojure.string :as string]
            [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [clojurewerkz.neocons.rest.transaction :as transaction]
            [taoensso.timbre :refer [spy]]
            [taoensso.timbre.profiling :refer [p defnp]]
            [schema.core :as s]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]
            [player-rankings.profiling :refer [timed]]
            [player-rankings.logic.rankings :as rankings]
            [player-rankings.logic.tournament-url-parser :as tournament-url-parser]
            [player-rankings.logic.tournament-constants :as constants]))

(def Aliases [s/Str])

(def MergeNode
  {:aid s/Int
   :bid s/Int
   :aliases Aliases})

(def MergeNodes
  [MergeNode])

(def Player
  {:aliases Aliases :id s/Int})

(def Players
  [Player])

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(defn- keys->keywords [coll]
  (into {} (for [[k v] coll] [(keyword k) v])))

(defn- create-tournament-node [tournament]
  (let [query (str "create (t:tournament {data}) "
                   "return id(t) as id")]
    ((first (cypher/tquery conn query {:data tournament})) "id")))

(defnp get-existing-players []
  (let [query (str "match (p:player) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))

(defnp get-players-for-rank-sorting []
  (let [query (str "match (p:player) "
                   "return id(p) as id, "
                   "p.aliases as aliases, "
                   "p.provisional_rating[0] as rating,"
                   "p.provisional_rating[1] as stddev")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))

(defn- create-new-player-nodes [player-names]
  (let [query (str "unwind {names} as name "
                   "create (p:player {name: name, aliases: [name]}) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query {:names player-names})]
    (map keys->keywords data)))

(defn- remove-common-team-names [lowercased-player-name team-names]
  (let [space-team-names (map #(str % " ") team-names)
        i-team-names (map #(str % "i") space-team-names)
        l-team-names (map #(str % "l") space-team-names)
        strings-to-remove (concat i-team-names l-team-names space-team-names)]
    (reduce (fn [acc team-name]
              (if (.startsWith acc team-name)
                (string/trim (string/replace-first acc team-name "")) acc))
            lowercased-player-name strings-to-remove)))

(defn normalize-name
  ([player-name] (normalize-name player-name constants/team-names))
  ([player-name team-names]
   (-> player-name
       (string/replace #"\(.*\)" "")
       (string/split #"\|")
       last
       string/trim
       string/lower-case
       (remove-common-team-names team-names)
       (string/replace #"\s" ""))))

(defn- get-matching-player [player-name players]
  (some #(when (some (fn [existing-player-name]
                       (= (normalize-name player-name)
                          (normalize-name existing-player-name)))
                     (map normalize-name (:aliases %))) %)
        players))

(defn- players-share-aliases? [a b]
  (let [a-aliases (set (map normalize-name (:aliases a)))
        b-aliases (set (map normalize-name (:aliases b)))]
    (not (empty? (intersection a-aliases b-aliases)))))

(defn- split-by-first-mergeable-player [players]
  (let [first-player (first players)]
    (reduce (fn [{:keys [matched unmatched]} player]
              (if (players-share-aliases? first-player player)
                {:matched (conj matched player) :unmatched unmatched}
                {:matched matched :unmatched (conj unmatched player)}))
            {:matched [] :unmatched []} players)))

(defn- merge-aliases [players]
  (let [aliases (mapcat :aliases players)]
    (reduce
     (fn [acc alias]
       (let [normalized-acc (map normalize-name acc)
             normalized-alias (normalize-name alias)]
         (if (some #(= normalized-alias %) normalized-acc)
           acc
           (conj acc alias)))) [] aliases)))

(defn- pair-merge-nodes-from-list [players]
  (let [aliases (merge-aliases players)
        canon-id (-> players first :id)
        merge-ids (map :id (rest players))]
    (reduce #(conj %1 {:aid canon-id :bid %2 :aliases aliases}) [] merge-ids)))

(defn- get-matching-players-from-alias-map
  ([alias-map player] (get-matching-players-from-alias-map alias-map player (:aliases player)))
  ([alias-map player aliases]
   (let [normalized-aliases (map normalize-name aliases)
         alias-to-check (first normalized-aliases)]
     (cond
       (empty? aliases) [player]
       (contains? alias-map alias-to-check) (conj (alias-map alias-to-check) player)
       :else (recur alias-map player (rest aliases))))))

(defn- add-player-to-alias-map [alias-map player]
  (let [matching-players (get-matching-players-from-alias-map alias-map player)
        aliases (merge-aliases matching-players)]
    (apply assoc (concat [alias-map]
                         (interleave (map normalize-name aliases) (repeat matching-players))))))

(defn- merge-in-alias-list [alias-map alias-list]
  (let [normalized-aliases (map normalize-name alias-list)
        matching-aliases (distinct (filter #(contains? alias-map %) normalized-aliases))
        merged-players (distinct (mapcat #(get alias-map %) matching-aliases))]
    (reduce #(assoc %1 %2 merged-players) alias-map matching-aliases)))

(defn- merge-players-by-explicit-alias [alias-map]
  (reduce (fn [coll alias-list]
            (merge-in-alias-list coll alias-list))
          alias-map constants/aliases))

(defn create-alias-map [players]
  (merge-players-by-explicit-alias
   (reduce #(add-player-to-alias-map %1 %2) {} players)))

(defn- create-merge-nodes-from-mergeable-players [mergeable-players]
  (let [players-to-merge (p :filter-empty-players (filter #(> (count %) 1) mergeable-players))]
    (mapcat pair-merge-nodes-from-list players-to-merge)))

(defn- match-aliases-to-players [aliases players]
  (let [alias-map (create-alias-map players)]
    (filter (comp not nil?)
            (distinct (mapcat #(get alias-map (normalize-name %) []) aliases)))))

(defn- has-multiple-elements? [coll]
  (> (count coll) 1))

(defn- filter-empty-aliases [matching-aliases]
  (filter has-multiple-elements? matching-aliases))

(defn- partition-by-explicit-players [players]
  (s/validate Players players)
  (filter (comp not empty?)
          (map #(match-aliases-to-players % players) constants/aliases)))

(defn- merge-nodes-into-db [merge-nodes]
  (s/validate MergeNodes merge-nodes)
  (let [query (str "unwind {records} as record "
                   "match (a:player), (b:player)-[bp:played]-(bm:match), "
                   "(b)-[pa:participated]-(t:tournament) "
                   "where id(a) = record.aid and id(b) = record.bid "
                   "set a.aliases = record.aliases "
                   "merge (a)-[:played {won: bp.won}]->(bm) "
                   "merge (a)-[:participated {placement: pa.placement}]->(t) "
                   "with a, b, pa, bp "
                   "delete b, pa, bp ")]
  (if (not (empty? merge-nodes))
    (cypher/tquery conn query {:records merge-nodes}))))

(defn partition-by-mergeable-players [players]
  (-> players create-alias-map vals distinct concat))

(defn- create-merge-nodes [players]
  (let [partitioned-players (partition-by-mergeable-players players)]
    (create-merge-nodes-from-mergeable-players partitioned-players)))

(defnp merge-player-nodes []
  (-> (get-existing-players)
      create-merge-nodes
      merge-nodes-into-db))

(defn- get-players-by-name-for-rank-sorting [player-names]
  (let [players (get-players-for-rank-sorting)
        alias-map (create-alias-map players)]
    (map (fn [player-name]
           (let [normalized-name (normalize-name player-name)]
             (if (contains? alias-map normalized-name)
               (assoc (first (get alias-map normalized-name)) :name player-name :new false)
               {:aliases [player-name]
                :rating (:rating rankings/default-rating)
                :stddev (:rd rankings/default-rating)
                :name player-name
                :new true})))
           player-names)))

(defn- normalize-player-scores [players]
  (map (fn [{:keys [rating stddev] :as player}]
         (let [min (- rating (* stddev 3))
               max (+ rating (* stddev 3))]
           (dissoc (assoc player :min min :max max) :rating :stddev :id)))
       players))

(defnp sort-player-names-by-cse [player-names]
  (->> player-names
       (get-players-by-name-for-rank-sorting)
       (normalize-player-scores)
       (sort-by :min)
       (reverse)))

(defn- create-player-nodes [matches]
  (let [first-players (map :player-one matches)
        second-players (map :player-two matches)
        unique-players (distinct (concat first-players second-players))]
    (create-new-player-nodes unique-players)))

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

(defnp raw-match-information []
  (let [query
        (str "match (player:player)-[played:played]-(game:match), "
             "(game)--(opponent:player)"
             "return id(player) as player_id, id(played) as played_id, game.score as score, "
             "id(opponent) as opponent_id, played.won as won, game.time as time "
             "order by game.time ")]
    (vec (cypher/tquery conn query))))

(defn- get-match-ratings []
  (-> (raw-match-information) rankings/ratings-from-matches))

(defn- update-played-with-ratings [match-ratings]
  (let [query (str "unwind {records} as record "
                   "match (p:player)-[pl:played]-(:match) "
                   "where id(p) = record.player_id and id(pl) = record.id "
                   "set pl.start_rating = [record.start.rating, "
                   "record.start.rd, record.start.volatility] "
                   "set pl.end_rating = [record.end.rating, "
                   "record.end.rd, record.end.volatility] ")]
    (cypher/tquery conn query {:records match-ratings})))

(defn- flatten-player-ratings [player-ratings]
  (reduce-kv (fn [coll k v]
               (conj coll (assoc v :id k)))
             [] player-ratings))

(defn- update-player-with-ratings [player-ratings]
  (let [vector-ratings (flatten-player-ratings player-ratings)
        query (str "unwind {records} as record "
                   "match (p:player) "
                   "where id(p) = record.id "
                   "set p.current_rating = [record.old.rating, "
                   "record.old.rd, record.old.volatility] "
                   "set p.provisional_rating = [record.current.rating, "
                   "record.current.rd, record.current.volatility] ")]
    (cypher/tquery conn query {:records vector-ratings})))

(defnp update-ratings []
  (let [ratings (get-match-ratings)]
    (update-played-with-ratings (:matches ratings))
    (update-player-with-ratings (:player-ratings ratings))))

(defn- create-ranked-records []
  (let [players (get-existing-players)
        ranked (set (map normalize-name constants/currently-ranked-players))
        previously-ranked (set (map normalize-name constants/previously-ranked-players))]
    (map (fn [player]
           (let [aliases (set (map normalize-name (:aliases player)))
                 id (:id player)]
             (cond (not= aliases (difference aliases ranked))
                   {:id id, :ranked "currently"}
                   (not= aliases (difference aliases previously-ranked))
                   {:id id :ranked "previously"}
                   :else
                   {:id id :ranked "unranked"})))
         players)))

(defnp update-rankings []
  (let [records (create-ranked-records)
        query (str "unwind {records} as record "
                   "match (p:player) "
                   "where id(p) = record.id "
                   "set p.ranked = record.ranked ")]
    (cypher/tquery conn query {:records records})))

(defn- merge-participants-with-tournament [tournament-id tournament-data]
  (let [query (str "unwind {players} as player "
                   "match (p:player {name: player.name}) "
                   "match (t:tournament) "
                   "where id(t) = {tournament_id} "
                   "create (p)-[:participated {placement: player.placement}]->(t) ")]
    (cypher/tquery conn query {:players (:participants tournament-data)
                               :tournament_id tournament-id})))

(defn- merge-matches-with-tournament [tournament-id tournament-data]
  (let [match-ids (create-match-graphs (:matches tournament-data))
        query (str "unwind {match_ids} as match_id "
                   "match (m:match) "
                   "where id(m) = match_id "
                   "match (t:tournament) "
                   "where id(t) = {tournament_id} "
                   "create (t)-[:hosted]->(m) ")]
    (cypher/tquery conn query {:match_ids match-ids
                               :players (:participants tournament-data)
                               :tournament_id tournament-id})))

(defnp create-tournament-graph [tournament-data]
  (spy :info (get-in tournament-data [:tournament :title]))
  (let [tournament-id (create-tournament-node (:tournament tournament-data))]
    (merge-matches-with-tournament tournament-id tournament-data)
    (merge-participants-with-tournament tournament-id tournament-data)))

(defnp load-tournaments [tournaments]
  (doseq [tournament tournaments]
    (create-tournament-graph tournament)))

(defnp update-player-data []
  (merge-player-nodes)
  (update-ratings)
  (update-rankings))

(defnp add-tournament [tournament-url]
  (let [tournament-data (tournament-url-parser/get-tournament-data tournament-url)]
    (create-tournament-graph tournament-data)
    (update-player-data)))