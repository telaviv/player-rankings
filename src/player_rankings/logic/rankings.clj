(ns player-rankings.logic.rankings
    (:import org.goochjs.glicko2.RatingCalculator
             org.goochjs.glicko2.Rating
             org.goochjs.glicko2.RatingPeriodResults))

(defn create-player [rating-system]
  (Rating. "Player" rating-system))

(defn- rating-to-map [rating-object]
  {:rating (.getRating rating-object)
   :rd (.getRatingDeviation rating-object)
   :volatility (.getVolatility rating-object)})

(defn calculate-rating-period [wins]
  (let [rating-system (RatingCalculator.)
        results (RatingPeriodResults.)
        player-to-track (create-player rating-system)
        dummy-player (create-player rating-system)]
    (doseq [win wins]
      (if win
        (.addResult results player-to-track dummy-player)
        (.addResult results dummy-player player-to-track)))
    (.updateRatings rating-system results)
    (rating-to-map player-to-track)))
