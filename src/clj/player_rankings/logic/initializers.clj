(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.tournament-url-parser :refer [get-tournament-data]]
            [taoensso.timbre :refer [spy]]
            [clj-time.coerce :as coerce-time]
            [clj-time.core :as time]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                test-urls]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    load-new-tournaments
                                                    update-player-data]]))

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
