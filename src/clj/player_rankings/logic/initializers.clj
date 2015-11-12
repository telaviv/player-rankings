(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.tournament-url-parser :refer [get-tournament-data]]
            [taoensso.timbre :refer [spy]]
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
  (load-data-from-tournaments tournament-urls))

(defn load-test-data []
  (load-data-from-tournaments test-urls))

(defn load-new-data []
  (load-data-from-tournaments (filter-tournament-date 2015 8 30)))
