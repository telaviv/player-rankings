(ns player-rankings.database.players.read
  (:require [player-rankings.database.connection :refer [cquery]]
            [clojure.string :as string]))

(defn- newline-joined [& lines]
  (string/join "\n" lines))


(defn- raw-tournament-data [identifier]
  (let [query (newline-joined
               "match (t:tournament {identifier: {identifier}})-[:hosted]-(m:match),"
               "(p1:player)-[pl1:played]-(m)-[pl2:played]-(p2:player)"
               "where pl1.won = true"
               "with t, "
               "{winner: {name: p1.name, start_rating: pl1.start_rating, end_rating: pl1.end_rating},"
               "loser: {name: p2.name, start_rating: pl2.start_rating, end_rating: pl2.end_rating},"
               "score: m.score, time: m.time, id: id(m)} as matches"
               "order by m.time"
               "return {title: t.title, url: t.url} as tournament, collect(matches) as matches")]
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
  (-> match
      (update :winner normalize-player)
      (update :loser normalize-player)))

(defn- normalize-matches [matches]
  (map normalize-match matches))

(defn median-time [matches]
  (get-in matches [(quot (count matches) 2) :time]))

(defn tournament-data [identifier]
  (let [data (raw-tournament-data identifier)]
    (-> data
        (update :matches normalize-matches)
        (assoc :time (median-time (:matches data))))))
