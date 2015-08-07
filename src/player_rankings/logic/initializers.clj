(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                test-urls]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    merge-player-nodes
                                                    update-ratings
                                                    update-rankings]]))

(defn update-player-data []
  (merge-player-nodes)
  (update-ratings)
  (update-rankings))

(defn load-data []
  (let [tournaments (map get-tournament-data tournament-urls)]
    (load-tournaments tournaments)
    (update-player-data)))

(defn load-test-data []
  (let [tournaments (map get-tournament-data test-urls)]
    (load-tournaments tournaments)
    (update-player-data)))
