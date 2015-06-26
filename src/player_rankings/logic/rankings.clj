(ns player-rankings.logic.rankings
  (:require [clojure.string :as string]
            [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
   (:import org.goochjs.glicko2.RatingCalculator
            org.goochjs.glicko2.Rating
            org.goochjs.glicko2.RatingPeriodResults))

(def DEFAULT-VOLATILITY 0.06)
(def DEFAULT-TAU 1.2)

(defn create-calculator []
  (RatingCalculator. DEFAULT-VOLATILITY DEFAULT-TAU))

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

(def default-rating (rating-to-map (Rating. "player" (create-calculator))))

(defn calculate-inactive-rating [initial-rating]
  (let [rating-system (create-calculator)
        results (RatingPeriodResults.)
        player (create-player initial-rating rating-system)]
    (.addParticipants results player)
    (.updateRatings rating-system results)
    {:old initial-rating, :current (rating-to-map player)}))

(defn calculate-rating-period [initial-rating matches]
  (let [rating-system (create-calculator)
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
            nmatch (nmatches i)
            match-info {:id (nmatch :id) :player_id (nmatch :player-id)}]
        (if (get-in nmatches [i :is-disqualified])
          (recur new-i old-rating (conj rating-coll
                                        (assoc match-info :start old-rating :end old-rating)))
          (let [new-rating (calculate-rating-period
                            initial-rating (take (inc i) nmatches))
                rating-diff (assoc match-info :start old-rating :end new-rating)]
            (recur new-i new-rating (conj rating-coll rating-diff))))))))


(defn group-matches-by-rating-period [matches]
  (let [sorted-matches (vec (sort-by #(% "time") matches))
        earliest-time (c/from-long (get-in sorted-matches [0 "time"]))]
    (vec (partition-by (fn [match]
                         (t/in-weeks (t/interval earliest-time
                                                 (c/from-long (match "time")))))
                       sorted-matches))))

(defn player-ids-from-matches [matches]
  (distinct (map #(% "player_id") matches)))

(defn group-matches-into-periods [matches]
  (let [matches-by-period (group-matches-by-rating-period matches)
        player-ids (player-ids-from-matches matches)]
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
   :player-id (match "player_id")
   :won (match "won")
   :opponent-rating (-> "opponent_id" match player-scores :current)
   :is-disqualified (-> "score" match is-disqualifying-score)})

(defn initial-player-ratings [player-ids]
  (zipmap player-ids (repeat {:old default-rating :current default-rating})))

(defn map-matches-with-ratings [matches player-ratings initial-rating]
  (->> matches
       (mapv #(normalize-match-for-calculation % player-ratings))
       (calculate-partial-ratings initial-rating)))

(defn map-ratings-to-period [player-ratings period]
  (reduce-kv (fn [coll k v]
               (assoc coll k (map-matches-with-ratings
                              v player-ratings (get-in player-ratings [k :current]))))
             {} period))

(defn aggregate-ratings-from-period [player-ratings period]
  (let [period-ratings (map-ratings-to-period player-ratings period)
        new-ratings (reduce-kv (fn [coll k v]
                                 (if (= 0 (count v))
                                   (assoc coll k (calculate-inactive-rating
                                                  (get-in player-ratings [k :current])))
                                   (assoc coll k {:old (-> v first :start)
                                                  :current (-> v last :end)})))
                               {} period-ratings)
        matches (-> period-ratings vals flatten)]
    {:player-ratings new-ratings :matches matches}))

(defn ratings-from-matches [matches]
  (let [periods (group-matches-into-periods matches)
        initial-ratings (-> matches player-ids-from-matches initial-player-ratings)]
    (reduce (fn [acc period]
              (let [new-ratings (aggregate-ratings-from-period (:player-ratings acc) period)]
                {:player-ratings (:player-ratings new-ratings)
                 :matches (concat (:matches acc) (:matches new-ratings))}))
            {:player-ratings initial-ratings :matches []} periods)))
