(ns player-rankings.actions
  (:require [clj-time.core :as t]
            [clj-time.local :as l]
            [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.database.players.read :as players]
            [player-rankings.logic.rankings :as rankings]))


(defn- normalize-player-scores [players]
  (map (fn [{:keys [rating stddev] :as player}]
         (let [min (- rating (* stddev 3))
               max (+ rating (* stddev 3))]
           (dissoc (assoc player :min min :max max) :rating :stddev :id)))
       players))

(defnp sort-player-names-by-cse [player-names]
  (->> player-names
       (players/get-players-by-name-for-rank-sorting)
       (normalize-player-scores)
       (sort-by :min)
       (reverse)))

(defn- timestamp-to-date [timestamp]
  (let [local (l/to-local-date-time timestamp)
        day (t/day local)
        month (t/month local)
        year (t/year local)]
    (format "%d-%d-%d" month day year)))

(defn- replace-time-with-date [match]
  (dissoc (assoc match :date (timestamp-to-date (:time match))) :time))

(defnp test-player-sorting [player-names]
  (sort-player-names-by-cse [player-names])
  (players/sort-players-by-cse [player-names]))
