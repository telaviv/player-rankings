(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [player-rankings.logic.tournament-constants :refer [tournament-urls aliases]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    merge-multiple-player-nodes
                                                    update-ratings]]))

(defn load-data []
  (let [tournaments (map get-tournament-data tournament-urls)]
    (load-tournaments tournaments)
    (merge-multiple-player-nodes aliases)
    (update-ratings)))
