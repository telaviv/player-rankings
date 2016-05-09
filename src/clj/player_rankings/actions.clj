(ns player-rankings.actions
  (:require [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.logic.database :as db]))

(defn- normalize-player-scores [players]
  (map (fn [{:keys [rating stddev] :as player}]
         (let [min (- rating (* stddev 3))
               max (+ rating (* stddev 3))]
           (dissoc (assoc player :min min :max max) :rating :stddev :id)))
       players))

(defnp sort-player-names-by-cse [player-names]
  (->> player-names
       (db/get-players-by-name-for-rank-sorting)
       (normalize-player-scores)
       (sort-by :min)
       (reverse)))
