(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [clj-time.coerce :as coerce-time]
            [clj-time.core :as time]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                test-urls]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    update-player-data]]))


(defn filter-tournament-date [year month day]
  (filter (fn [tournament]
            (time/after? (-> (get-in tournament [:tournament :started_at]) coerce-time/from-long)
                         (time/date-time year month day)))
          (pmap get-tournament-data tournament-urls)))

(defn load-data-from-tournaments [tournaments]
  (load-tournaments tournaments)
  (update-player-data))

(defn load-data []
  (->> tournament-urls (pmap get-tournament-data) load-data-from-tournaments))

(defn load-test-data []
  (->> tournament-urls butlast (pmap get-tournament-data) load-data-from-tournaments))

(defn load-summer-data []
  (->> (filter-tournament-date 2015 6 28) load-data-from-tournaments))
