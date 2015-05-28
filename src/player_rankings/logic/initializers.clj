(ns player-rankings.logic.initializers
  (:require [player-rankings.logic.challonge-parser :refer [get-tournament-data]]
            [player-rankings.logic.database :refer [load-tournaments
                                                    merge-multiple-player-nodes
                                                    update-ratings]]))

(def tournament-urls
  ["http://showdowngg.challonge.com/comeonandban13singles"
   "http://showdowngg.challonge.com/comeonandban14singles"
   "http://showdowngg.challonge.com/comeonandban15singles"
   "http://showdowngg.challonge.com/comeonandban16singles"
   "http://showdowngg.challonge.com/comeonandban17singles"
   "http://showdowngg.challonge.com/comeonandban18singles"
   "http://challonge.com/MADE5Singles"
   "http://challonge.com/MADE6Singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/BMLH3"
   "http://capitolfightdistrict.challonge.com/cfdandgplswu"
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://challonge.com/minibosssmash4singles2"
   "http://challonge.com/hostdc"
   "http://challonge.com/hostdc1singles"
   "http://challonge.com/hostdc2singles"
   "http://challonge.com/hostdc3singles"
   "http://challonge.com/hostdc4singles"
   "http://ncr2015.challonge.com/SSB41"
   "http://ncr2015.challonge.com/SSB42"
   "http://ncr2015.challonge.com/SSB43"
   "http://ncr2015.challonge.com/SSB44"
   "http://ncr2015.challonge.com/SSB45"
   "http://ncr2015.challonge.com/SSB46"
   "http://ncr2015.challonge.com/SSB47"
   "http://ncr2015.challonge.com/SSB48"
   "http://ncr2015.challonge.com/SSB416"
   "http://bko.challonge.com/SO34"
   "http://bko.challonge.com/SO44"])

(def aliases
  [["Crow" "Chaos Crow"]
   ["Mijo" "Mijo FUEGO"]
   ["Mr. Javi" "Mr Jav"]
   ["SKS" "watislyfe"]
   ["SKS" "SKS aka Watislyfe"]
   ["NME | Nanerz" "Cynthia"]
   ["Rickshaw" "NinjaRlink"]
   ["Rickshaw" "Rickshaw (set)"]
   ["BaNdt" "ARaNdomVillager (BaNdt)"]
   ["Arikie" "MS | Shadow"]
   ["GShark" "Leffen_Shark"]
   ["Scourge" "Summus (Scourge)"]])

(defn load-data []
  (let [tournaments (map get-tournament-data tournament-urls)]
    (load-tournaments tournaments)
    (merge-multiple-player-nodes aliases)
    (update-ratings)))
