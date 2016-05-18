(ns player-rankings.database.tournaments
  (:require [clojure.set :refer [difference]]
            [clojure.data.json :as json]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce-time]
            [taoensso.timbre :refer [spy info]]
            [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.database.players.read :refer [normalize-name]]
            [player-rankings.database.players.write :refer [create-new-player-nodes]]
            [player-rankings.logic.database :refer :all]
            [player-rankings.logic.rankings :as rankings]
            [player-rankings.logic.tournament-url-parser :as tournament-url-parser]
            [player-rankings.logic.tournament-constants :as constants]
            [player-rankings.utilities :refer [keys->keywords]]))

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

(defnp cache-tournament-data [tournament-data]
  (cypher/tquery conn
                 "create (tc:tournament_cache {blob: {tournament_data}})"
                 {:tournament_data (json/write-str tournament-data)}))

(defnp load-tournament-cache []
  (let [query (str "match (tc:tournament_cache) "
                   "return tc.blob as blob")
        tournament-cache (cypher/tquery conn query)]
    (reduce (fn [coll cache]
              (let [tournament (json/read-str (cache "blob") :key-fn keyword)]
                (assoc coll (get-in tournament [:tournament :url]) tournament)))
            {} tournament-cache)))

(defn- create-tournament-node [tournament]
  (let [query (str "create (t:tournament {data}) "
                   "return id(t) as id")]
    ((first (cypher/tquery conn query {:data tournament})) "id")))

(defnp create-tournament-graph [tournament-data]
  (spy :info (get-in tournament-data [:tournament :title]))
  (let [tournament-id (create-tournament-node (:tournament tournament-data))]
    (merge-matches-with-tournament tournament-id tournament-data)
    (merge-participants-with-tournament tournament-id tournament-data)))

(defnp delete-uncached-data []
  (let [query (str "match (p:player)--(t:tournament), (t)--(m:match) "
                   "detach delete p, t, m")]
    (cypher/tquery conn query)))

(defnp load-tournament-data
  ([meta-url] (load-tournament-data meta-url (load-tournament-cache)))
  ([meta-url tournament-cache]
   (let [tournament-url (tournament-url-parser/url-from-meta-url meta-url)]
     (if (contains? tournament-cache tournament-url)
       (do (info (str "cache hit: " tournament-url))
           (tournament-cache tournament-url))
       (do (info (str "cache miss: " tournament-url))
           (let [tournament-data
                 (tournament-url-parser/get-tournament-data meta-url)]
             (cache-tournament-data tournament-data)
             tournament-data))))))

(defnp get-loaded-tournament-urls []
  (set (map #(get % "url") (cypher/tquery conn "match (t:tournament) return t.url as url"))))

(defnp get-new-tournament-urls [year month day]
  (let [cache (load-tournament-cache)]
    (map (fn [tournament] (get-in tournament [:tournament :url]))
         (filter (fn [tournament]
                   (time/after?
                    (-> (get-in tournament [:tournament :started_at]) coerce-time/from-long)
                    (time/date-time year month day)))
                 (vals cache)))))

(defnp remove-loaded-tournaments [meta-urls]
  (let [existing-urls (get-loaded-tournament-urls)]
    (filter #(not (contains? existing-urls (tournament-url-parser/url-from-meta-url %)))
            meta-urls)))

(defnp load-tournaments [tournaments]
  (let [urls-to-load (remove-loaded-tournaments tournaments)
        tournament-cache (load-tournament-cache)
        tournament-datum (map #(load-tournament-data % tournament-cache) urls-to-load)]
    (doseq [tournament-data tournament-datum]
      (create-tournament-graph tournament-data))))

(defnp load-new-tournaments [year month day]
  (-> (get-new-tournament-urls year month day) load-tournaments))

(defnp update-player-data []
  (merge-player-nodes)
  (update-ratings)
  (update-rankings))

(defnp add-tournament [tournament-url]
  (load-tournaments [tournament-url])
  (update-player-data))
