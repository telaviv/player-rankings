(ns player-rankings.logic.initializers
  (:require [player-rankings.parsers.tournament-urls :refer [get-tournament-data]]
            [taoensso.timbre :refer [spy]]
            [clj-time.coerce :as coerce-time]
            [clj-time.core :as time]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                test-urls]]
            [player-rankings.database.tournaments :refer [load-tournaments
                                                          load-new-tournaments
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
  (load-data-from-tournaments tournament-urls))

(defn load-test-data []
  (load-data-from-tournaments test-urls))

(defn load-new-data []
  (load-new-tournaments 2015 10 30)
  (update-player-data))
