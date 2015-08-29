(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                test-urls]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    update-player-data]]))



(defn load-data []
  (let [tournaments (pmap get-tournament-data tournament-urls)]
    (load-tournaments tournaments)
    (update-player-data)))

(defn load-test-data []
  (let [tournaments (pmap get-tournament-data (butlast tournament-urls))]
    (load-tournaments tournaments)))
