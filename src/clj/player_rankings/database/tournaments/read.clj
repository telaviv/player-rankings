(ns player-rankings.database.tournaments.read
  (:require [player-rankings.database.connection :refer [cquery]]
            [player-rankings.logic.rankings :as rankings]
            [clojure.string :as string]))

(defn- newline-joined [& lines]
  (string/join "\n" lines))


(defn- raw-tournament-data [identifier]
  (let [query (newline-joined
               "match (t:tournament {identifier: {identifier}})-[:hosted]-(m:match),"
               "(p1:player)-[pl1:played]-(m)-[pl2:played]-(p2:player)"
               "where pl1.won = true"
               "with t, "
               "{winner: p1.name, loser: p2.name, score: m.score, time: m.time, id: id(m)} as matches"
               "order by m.time"
               "return {title: t.title, url: t.url, id: t.identifier} as tournament, collect(matches) as matches")]
    (first (cquery query {:identifier identifier}))))

(defn- rating-array-to-object [rating-array]
  {:rating (get rating-array 0)
   :stddev (get rating-array 1)
   :volatility (get rating-array 2)})

(defn- normalize-player [player]
  (-> player
      (update :start_rating rating-array-to-object)
      (update :end_rating rating-array-to-object)))

(defn- normalize-match [match]
  (let [winner (normalize-player (:winner match))
        loser (normalize-player (:loser match))
        win-percentage (rankings/win-percentage (:start_rating winner)
                                                (:start_rating loser))]
    (assoc match
           :winner winner
           :loser loser
           :win-percentage win-percentage)))

(defn- normalize-matches [matches]
  (map normalize-match matches))

(defn median-time [matches]
  (get-in matches [(quot (count matches) 2) :time]))

(defn tournament-data [identifier]
  (let [data (raw-tournament-data identifier)]
    (-> data
        (assoc-in [:tournament :time] (median-time (:matches data))))))

(defn tournament-exists? [identifier]
  (let [query (newline-joined
               "match (t:tournament {identifier: {identifier}})"
               "return count(*) as count")
        c (:count (first (cquery query {:identifier identifier})))]
    (not (= 0 c))))
