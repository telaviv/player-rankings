(ns player-rankings.logic.database
  (:require [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.database.players.read :refer :all]))

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
