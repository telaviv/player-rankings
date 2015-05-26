(ns player-rankings.logic.rankings
    (:import org.goochjs.glicko2.RatingCalculator
             org.goochjs.glicko2.Rating
             org.goochjs.glicko2.RatingPeriodResults))

(defn create-player [rating-system]
  (Rating. "player" rating-system))

(defn- rating-to-map [rating-object]
  {:rating (.getRating rating-object)
   :rd (.getRatingDeviation rating-object)
   :volatility (.getVolatility rating-object)})

(defn calculate-rating-period [wins]
  (let [rating-system (RatingCalculator.)
        results (RatingPeriodResults.)
        player-to-track (create-player rating-system)
        dummy-player (create-player rating-system)]
    (.addParticipants results player-to-track)
    (doseq [win wins]
      (if win
        (.addResult results player-to-track dummy-player)
        (.addResult results dummy-player player-to-track)))
    (.updateRatings rating-system results)
    (rating-to-map player-to-track)))

(defn calculate-partial-ratings [wins]
  (loop [i 1
         old-rating (-> (RatingCalculator.) create-player rating-to-map)
         rating-coll []]
    (if (> i (count wins))
      rating-coll
      (let [new-rating (calculate-rating-period (subvec wins 0 i))
            rating-diff {:start old-rating :end new-rating}]
        (recur (inc i) new-rating (conj rating-coll rating-diff))))))

(defn- is-disqualifying-score [score]
  (let [score-parts (-> score
                        (string/replace #"(-?\d)-(-?\d)" "$1 $2")
                        (string/split #" "))]
    (->> score-parts
         (map read-string)
         (some #(< % 0))
         (= true))))
