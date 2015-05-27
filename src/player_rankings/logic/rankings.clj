(ns player-rankings.logic.rankings
  (:require [clojure.string :as string]
            [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
   (:import org.goochjs.glicko2.RatingCalculator
            org.goochjs.glicko2.Rating
            org.goochjs.glicko2.RatingPeriodResults))


(defn create-player [rating rating-system]
  (let [player (Rating. "player" rating-system)]
    (.setRating player (rating :rating))
    (.setRatingDeviation player (rating :rd))
    (.setVolatility player (rating :volatility))
    player))

(defn- rating-to-map [rating-object]
  {:rating (.getRating rating-object)
   :rd (.getRatingDeviation rating-object)
   :volatility (.getVolatility rating-object)})

(def default-rating (rating-to-map (Rating. "player" (RatingCalculator.))))

(defn calculate-rating-period [initial-rating matches]
  (let [rating-system (RatingCalculator.)
        results (RatingPeriodResults.)
        player (create-player initial-rating rating-system)]
    (.addParticipants results player)
    (doseq [match matches]
      (let [opponent (create-player (:opponent-rating match) rating-system)]
        (if (:won match)
          (.addResult results player opponent)
          (.addResult results opponent player))))
    (.updateRatings rating-system results)
    (rating-to-map player)))

(defn calculate-partial-ratings [initial-rating nmatches]
  (loop [i 0
         old-rating initial-rating
         rating-coll []]
    (if (= i (count nmatches))
      rating-coll
      (let [new-i (inc i)
            match-id (get-in nmatches [i :id])]
        (if (get-in nmatches [i :is-disqualified])
          (recur new-i old-rating (conj rating-coll
                                        {:start old-rating :end old-rating :id match-id}))
          (let [new-rating (calculate-rating-period
                            initial-rating (take (inc i) nmatches))
                rating-diff {:start old-rating :end new-rating :id match-id}]
            (recur new-i new-rating (conj rating-coll rating-diff))))))))


(defn group-matches-by-rating-period [matches]
  (let [time-seq (rest (p/periodic-seq (c/from-long (get-in matches [0 "time"])) (t/weeks 2)))]
    (:groups (reduce (fn [acc match]
                       (if (t/before? (c/from-long (match "time")) (first (:times acc)))
                         (assoc acc :current-group (conj (:current-group acc) match))
                         {:times (rest (:times acc))
                          :groups (conj (:groups acc) (:current-group acc))
                          :current-group []}))
                     {:times time-seq :groups [] :current-group []} matches))))

(defn group-matches-by-player-and-period [matches]
  (let [matches-by-period (group-matches-by-rating-period matches)
        player-ids (distinct (map #(% "player_id") matches))]
    (mapv (fn [matches-in-period]
           (into {}
                 (map (fn [p] {p (filter #(= p (% "player_id")) matches-in-period)})
                      player-ids)))
         matches-by-period)))

(defn- is-disqualifying-score [score]
  (let [score-parts (-> score
                        (string/replace #"(-?\d)-(-?\d)" "$1 $2")
                        (string/split #" "))]
    (->> score-parts
         (map read-string)
         (some #(< % 0))
         (= true))))

(defn normalize-match-for-calculation [match player-scores]
  {:id (match "played_id")
   :won (match "won")
   :opponent-rating (player-scores (match "opponent_id"))
   :is-disqualified (is-disqualifying-score (match "score"))})

(defn initial-player-ratings [player-ids]
  (zipmap player-ids (repeat default-rating)))

(defn map-matches-with-ratings [matches player-ratings initial-rating]
  (->> matches
       (mapv #(normalize-match-for-calculation % player-ratings))
       (calculate-partial-ratings initial-rating)))

(defn aggregate-period [player-ratings period]
  (reduce-kv (fn [coll k v]
               (assoc coll k (map-matches-with-ratings
                              v player-ratings (player-ratings k))))
             {} period))
