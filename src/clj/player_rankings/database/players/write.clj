(ns player-rankings.database.players.write
  (:require [clojure.set :refer [intersection]]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [schema.core :as s]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.database.players.read :as players]
            [player-rankings.logic.tournament-constants :as constants]
            [player-rankings.utilities :refer [keys->keywords]]
            [taoensso.timbre.profiling :refer [p defnp]]))

(def Aliases [s/Str])

(def MergeNode
  {:aid s/Int
   :bid s/Int
   :aliases Aliases
   :normalized Aliases})

(def MergeNodes
  [MergeNode])

(def Player
  {:aliases Aliases :id s/Int})

(def Players
  [Player])

(defn- players-share-aliases? [a b]
  (let [a-aliases (set (map players/normalize-name (:aliases a)))
        b-aliases (set (map players/normalize-name (:aliases b)))]
    (not (empty? (intersection a-aliases b-aliases)))))

(defn- split-by-first-mergeable-player [players]
  (let [first-player (first players)]
    (reduce (fn [{:keys [matched unmatched]} player]
              (if (players-share-aliases? first-player player)
                {:matched (conj matched player) :unmatched unmatched}
                {:matched matched :unmatched (conj unmatched player)}))
            {:matched [] :unmatched []} players)))

(defn- pair-merge-nodes-from-list [players]
  (let [aliases (players/merge-aliases players)
        normalized (map players/normalize-name aliases)
        canon-id (-> players first :id)
        merge-ids (map :id (rest players))]
    (reduce #(conj %1
                   {:aid canon-id :bid %2 :aliases aliases :normalized normalized})
            [] merge-ids)))

(defn- create-merge-nodes-from-mergeable-players [mergeable-players]
  (let [players-to-merge (p :filter-empty-players (filter #(> (count %) 1) mergeable-players))]
    (mapcat pair-merge-nodes-from-list players-to-merge)))

(defn- match-aliases-to-players [aliases players]
  (let [alias-map (players/create-alias-map players)]
    (filter (comp not nil?)
            (distinct (mapcat #(get alias-map (players/normalize-name %) []) aliases)))))

(defn- has-multiple-elements? [coll]
  (> (count coll) 1))

(defn- filter-empty-aliases [matching-aliases]
  (filter has-multiple-elements? matching-aliases))

(defn- partition-by-explicit-players [players]
  (s/validate Players players)
  (filter (comp not empty?)
          (map #(match-aliases-to-players % players) constants/aliases)))

(defn partition-by-mergeable-players [players]
  (-> players players/create-alias-map vals distinct concat))

(defn- merge-nodes-into-db [merge-nodes]
  (s/validate MergeNodes merge-nodes)
  (let [query (str "unwind {records} as record "
                   "match (a:player), "
                   "(b:player)-[bp:played]-(bm:match), "
                   "(b)-[pa:participated]-(t:tournament), "
                   "(b)-[bat:aliased_to]-(:alias) "
                   "where id(a) = record.aid and id(b) = record.bid "
                   "set a.aliases = record.aliases "
                   "merge (a)-[:played {won: bp.won}]->(bm) "
                   "merge (a)-[:participated {placement: pa.placement}]->(t) "
                   "with a, b, pa, bp, bat, record "
                   "delete b, pa, bp, bat "
                   "with a, record.normalized as aliases "
                   "unwind aliases as alias "
                   "merge (al:alias {name: alias}) "
                   "merge (a)-[:aliased_to]->(al) ")]
  (if (not (empty? merge-nodes))
    (cypher/tquery conn query {:records merge-nodes}))))

(defn- create-merge-nodes [players]
  (let [partitioned-players (partition-by-mergeable-players players)]
    (create-merge-nodes-from-mergeable-players partitioned-players)))

(defnp merge-player-nodes []
  (-> (players/get-existing-players)
      create-merge-nodes
      merge-nodes-into-db))

(defn create-new-player-nodes [player-names]
  (let [query (str "unwind {records} as record "
                   "create (p:player {name: record.name, aliases: [record.name]}) "
                   "create (a:alias {name: record.normalized})<-[:aliased_to]-(p) "
                   "return id(p) as id, p.aliases as aliases")
        normalized-names (map players/normalize-name player-names)
        records (map (fn [p n] {:name p :normalized n}) player-names normalized-names)
        data (cypher/tquery conn query {:records records})]
    (map keys->keywords data)))
