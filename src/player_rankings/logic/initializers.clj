(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [player-rankings.logic.tournament-constants :refer [tournament-urls
                                                                summer-all-urls
                                                                aliases]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    merge-multiple-player-nodes
                                                    update-ratings
                                                    update-rankings]]))

(defn load-data []
  (let [tournaments (map get-tournament-data tournament-urls)]
    (load-tournaments tournaments)
    (merge-multiple-player-nodes aliases)
    (update-ratings)
    (update-rankings)))

(defn load-summer-data []
  (let [tournaments (map get-tournament-data summer-all-urls)]
    (load-tournaments tournaments)
    (merge-multiple-player-nodes aliases)
    (update-ratings)
    (update-rankings)))
