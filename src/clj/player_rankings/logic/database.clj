(ns player-rankings.logic.database
  (:require [clojure.set :refer [intersection]]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [clojurewerkz.neocons.rest.transaction :as transaction]
            [taoensso.timbre.profiling :refer [p defnp]]
            [schema.core :as s]
            [player-rankings.profiling :refer [timed]]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.database.players.read :refer :all]
            [player-rankings.logic.rankings :as rankings]
            [player-rankings.logic.tournament-constants :as constants]
            [player-rankings.utilities :refer [keys->keywords]]))

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

(defnp get-existing-players []
  (let [query (str "match (p:player) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))


(defn get-matching-player [player-name players]
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

(defn- pair-merge-nodes-from-list [players]
  (let [aliases (merge-aliases players)
        canon-id (-> players first :id)
        merge-ids (map :id (rest players))]
    (reduce #(conj %1 {:aid canon-id :bid %2 :aliases aliases}) [] merge-ids)))

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

(defnp filtered-crew-members [crew existing-players]
  (let [crew-scores (map #(get-matching-player % existing-players) crew)]
    (filter #(< (:stddev %) 100) crew-scores)))

(defnp top-players-in-crew [crew-scores]
  (take 5 (reverse (sort-by :rating crew-scores))))

(defnp score-top-players-in-crew [crew-scores]
  (/ (reduce + (map :rating (top-players-in-crew crew-scores))) 5))

(defnp score-crews [crews existing-players]
  (reduce-kv
   (fn [coll crew-name crew]
     (let [crew-scores (filtered-crew-members crew existing-players)]
       (if (>= (count crew-scores) 5)
         (assoc coll crew-name {:score (score-top-players-in-crew crew-scores)})
         coll)))
   {} crews))

(defnp crew-scores [crews]
  (let [player-names (-> crews vals flatten)
        existing-players (get-players-by-name-for-rank-sorting player-names)]
    (score-crews crews existing-players)))
