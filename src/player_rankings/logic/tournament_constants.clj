(ns player-rankings.logic.tournament-constants
  (:require [clojure.set :refer [difference]]))

(def tournament-urls
  ["http://showdowngg.challonge.com/comeonandban13singles"
   "http://showdowngg.challonge.com/comeonandban2singles"
   "http://showdowngg.challonge.com/comeonandban3singles"
   "http://showdowngg.challonge.com/comeonandban4singles"
   "http://showdowngg.challonge.com/comeonandban5singles"
   "http://showdowngg.challonge.com/comeonandban6singles"
   "http://showdowngg.challonge.com/comeonandban7singles"
   "http://showdowngg.challonge.com/comeonandban8singles"
   "http://showdowngg.challonge.com/comeonandban9singles"
   "http://showdowngg.challonge.com/comeonandban12singles"
   "http://showdowngg.challonge.com/comeonandban10singles"
   "http://showdowngg.challonge.com/comeonandban14singles"
   "http://showdowngg.challonge.com/comeonandban15singles"
   "http://showdowngg.challonge.com/comeonandban16singles"
   "http://showdowngg.challonge.com/comeonandban17singles"
   "http://showdowngg.challonge.com/comeonandban18singles"
   "http://showdowngg.challonge.com/comeonandban19singles"
   "http://showdowngg.challonge.com/comeonandban20singles"
   "http://showdowngg.challonge.com/comeonandban21singles"
   "http://showdowngg.challonge.com/comeonandban22singles"
   "http://showdowngg.challonge.com/comeonandban23singles"
   "http://challonge.com/MADE5Singles"
   "http://challonge.com/MADE6Singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/MADE8Singles"
   "http://challonge.com/MADE9Singles"
   "http://challonge.com/MADE10Singles"
   "http://challonge.com/BMLC1"
   "http://challonge.com/BMLC2"
   "http://challonge.com/BMLC3"
   "http://challonge.com/BMLC4"
   "http://challonge.com/BMLC5"
   "http://challonge.com/BMLC6"
   "http://challonge.com/BMLC7"
   "http://challonge.com/BMLH3"
   "http://challonge.com/BMLH6"
   "http://challonge.com/BMLH5"
   "http://challonge.com/BMLH4"
   "http://challonge.com/BMLH2"
   "http://challonge.com/BMLH1"
   "http://scusmash.challonge.com/WW14_4SINGLES"
   "http://scusmash.challonge.com/WW13_4Singles"
   "http://scusmash.challonge.com/WW11S4"
   "http://scusmash.challonge.com/wwFinale_smash4"
   "http://capitolfightdistrict.challonge.com/cfdandgplswu"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://capitolfightdistrict.challonge.com/alubwcl7"
   "http://capitolfightdistrict.challonge.com/hn1tvh5t"
   "http://capitolfightdistrict.challonge.com/icrrkb29"
   "http://capitolfightdistrict.challonge.com/1vzpjxd1"
   "http://capitolfightdistrict.challonge.com/oojlvpxf"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://challonge.com/minibosssmash4singles2"
   "http://challonge.com/hostdc"
   "http://challonge.com/hostdc1singles"
   "http://challonge.com/hostdc2singles"
   "http://challonge.com/hostdc3singles"
   "http://challonge.com/hostdc4singles"
   "http://challonge.com/hostdc5singles"
   "http://challonge.com/hostdc6singles"
   "http://challonge.com/hostdc7singles"
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
   "http://bko.challonge.com/SO44"
   "http://bko.challonge.com/BMS4S"
   "http://bko.challonge.com/SO5M"
   "http://bko.challonge.com/OW3S4"])

(def summer-power-ranks-urls
  ["http://BKO.challonge.com/BMS4S"
   "http://BKO.challonge.com/SO44"
   "http://bko.challonge.com/OW3S4"
   "http://SCUsmash.challonge.com/wwFinale_smash4"
   "http://capitolfightdistrict.challonge.com/icrrkb29"
   "http://challonge.com/BMLC4"
   "http://challonge.com/BMLC5"
   "http://challonge.com/BMLC6"
   "http://challonge.com/BMLH1"
   "http://challonge.com/BMLH3"
   "http://challonge.com/BMLH5"
   "http://challonge.com/BMLH6"
   "http://challonge.com/MADE5Singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/MADE9Singles"
   "http://challonge.com/BMLC1"
   "http://challonge.com/BMLC3"
   "http://challonge.com/BMLC7"
   "http://challonge.com/BMLH4"
   "http://challonge.com/MADE6Singles"
   "http://challonge.com/MADE8Singles"
   "http://challonge.com/MADE10Singles"
   "http://challonge.com/hostdc"
   "http://challonge.com/hostdc3singles"
   "http://challonge.com/hostdc5singles"
   "http://challonge.com/hostdc6singles"
   "http://challonge.com/minibosssmash4singles2"
   "http://challonge.com/BMLC2"
   "http://challonge.com/hostdc7singles"
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://ncr2015.challonge.com/SSB41"
   "http://ncr2015.challonge.com/SSB42"
   "http://ncr2015.challonge.com/SSB43"
   "http://ncr2015.challonge.com/SSB44"
   "http://ncr2015.challonge.com/SSB45"
   "http://ncr2015.challonge.com/SSB46"
   "http://ncr2015.challonge.com/SSB47"
   "http://ncr2015.challonge.com/SSB48"
   "http://ncr2015.challonge.com/SSB416"
   "http://showdowngg.challonge.com/comeonandban12singles"
   "http://showdowngg.challonge.com/comeonandban13singles"
   "http://showdowngg.challonge.com/comeonandban14singles"
   "http://showdowngg.challonge.com/comeonandban15singles"
   "http://showdowngg.challonge.com/comeonandban16singles"
   "http://showdowngg.challonge.com/comeonandban17singles"
   "http://showdowngg.challonge.com/comeonandban18singles"
   "http://showdowngg.challonge.com/comeonandban19singles"
   "http://showdowngg.challonge.com/comeonandban20singles"
   "http://showdowngg.challonge.com/comeonandban21singles"
   "http://showdowngg.challonge.com/comeonandban22singles"
   "http://showdowngg.challonge.com/comeonandban23singles"])

(def summer-all-urls
  ["http://showdowngg.challonge.com/comeonandban13singles"
   "http://showdowngg.challonge.com/comeonandban14singles"
   "http://showdowngg.challonge.com/comeonandban15singles"
   "http://showdowngg.challonge.com/comeonandban16singles"
   "http://showdowngg.challonge.com/comeonandban19singles"
   "http://showdowngg.challonge.com/comeonandban20singles"
   "http://showdowngg.challonge.com/comeonandban21singles"
   "http://showdowngg.challonge.com/comeonandban23singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/MADE8Singles"
   "http://challonge.com/MADE9Singles"
   "http://challonge.com/MADE10Singles"
   "http://challonge.com/BMLC1"
   "http://challonge.com/BMLC3"
   "http://challonge.com/BMLC4"
   "http://challonge.com/BMLC5"
   "http://challonge.com/BMLC6"
   "http://challonge.com/BMLC7"
   "http://challonge.com/BMLH3"
   "http://challonge.com/BMLH5"
   "http://challonge.com/BMLH4"
   "http://SCUsmash.challonge.com/WW11S4"
   "http://SCUsmash.challonge.com/wwFinale_smash4"
   "http://capitolfightdistrict.challonge.com/cfdandgplswu"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://capitolfightdistrict.challonge.com/alubwcl7"
   "http://capitolfightdistrict.challonge.com/1vzpjxd1"
   "http://capitolfightdistrict.challonge.com/oojlvpxf"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://challonge.com/minibosssmash4singles2"
   "http://challonge.com/hostdc5singles"
   "http://challonge.com/hostdc6singles"
   "http://ncr2015.challonge.com/SSB41"
   "http://ncr2015.challonge.com/SSB44"
   "http://ncr2015.challonge.com/SSB45"
   "http://ncr2015.challonge.com/SSB46"
   "http://ncr2015.challonge.com/SSB47"
   "http://ncr2015.challonge.com/SSB48"
   "http://ncr2015.challonge.com/SSB416"
   "http://BKO.challonge.com/SO34"
   "http://BKO.challonge.com/SO44"
   "http://BKO.challonge.com/BMS4S"
   "http://BKO.challonge.com/SO5M"
   "http://bko.challonge.com/OW3S4"
   "http://showdowngg.challonge.com/comeonandban18singles"
   "http://ncr2015.challonge.com/SSB42"
   "http://challonge.com/hostdc4singles"
   "http://showdowngg.challonge.com/comeonandban17singles"
   "http://capitolfightdistrict.challonge.com/hn1tvh5t"
   "http://capitolfightdistrict.challonge.com/icrrkb29"
   "http://challonge.com/hostdc1singles"
   "http://challonge.com/hostdc7singles"
   "http://challonge.com/hostdc2singles"
   "http://challonge.com/hostdc3singles"
   "http://challonge.com/MADE6Singles"
   "http://showdowngg.challonge.com/comeonandban12singles"
   "http://challonge.com/BMLH6"
   "http://challonge.com/MADE5Singles"
   "http://SCUsmash.challonge.com/WW13_4Singles"
   "http://SCUsmash.challonge.com/WW14_4SINGLES"
   "http://showdowngg.challonge.com/comeonandban22singles"
   "http://challonge.com/hostdc"
   "http://challonge.com/BMLC2"
   "http://ncr2015.challonge.com/SSB43"])

(def team-names
  ["sky raiders" "1up" "nme" "bask" "pho" "bko" "made" "8bit" "swarm" "uc" "fyt"])

(def aliases
  [["Crow" "Chaos Crow"]
   ["Crow" "Maleficent"]
   ["Mijo" "Mijo FUEGO"]
   ["Mijo" "Fuego"]
   ["Mr. Javi" "Mr Jav"]
   ["SKS" "watislyfe"]
   ["SKS" "SKS aka Watislyfe"]
   ["NME | Nanerz" "Cynthia"]
   ["Rickshaw" "NinjaRlink"]
   ["BaNdt" "ARaNdomVillager"]
   ["Arikie" "MS | Shadow"]
   ["Arikie" "4B_Arikie"]
   ["GShark" "Leffen_Shark"]
   ["GShark" "G-Shark"]
   ["Mocha" "Andrew Le"]
   ["Scourge" "Summus"]
   ["Kronos" "Kronos2560"]
   ["Hitaku" "Hitaku Back Sunday"]
   ["Boba Tapioca" "Boba"]
   ["UC | DSS" "@UC_DSS"]
   ["Jimber Jangers" "Daimyes"]
   ["MisterQ" "MrQ"]
   ["MisterQ" "Mr. Q"]
   ["Chaos Pro" "Sm4sh Mango aka Chaos Pro"]
   ["Chaos Pro" "Smash 4 Mango aka ChaosPro"]
   ["Rice" "Rice-kun"]
   ["Andy_Sauro" "Andy The Albatross"]
   ["Focast" "Foucast"]
   ["Chinito" "Chihito"]
   ["Jeepysol" "Jeepy"]
   ["Trex Destiny" "T-Rex Destiny"]
   ["Villain" "Villian"]])

(def january-power-ranks
  ["Ito"
   "Sean"
   "Rice"
   "TwicH"
   "Scourge"
   "Silent Spectre"
   "Boba Tapioca"
   "Pheonix D"
   "Pump Magic"
   "DSS"
   "Choice"
   "JustTakeIt"
   "X"
   "Mr. Krabs"
   "Nitro"])

(def february-power-ranks
  ["Sean"
   "Jeepy"
   "Choice"
   "DSS"
   "Rice"
   "Pump Magic"
   "Boba Tapioca"
   "Twich"
   "Scourge"
   "C4"
   "Phancy"
   "Trevonte"
   "X"
   "Nitro"
   "Kronos"])

(def march-power-ranks
  ["Zex"
   "Sean"
   "Trevonte"
   "DSS"
   "Legit"
   "X"
   "Silent Spectre"
   "Rice"
   "Nitro"
   "Bandt"
   "C4"
   "Jeepy"
   "Pump Magic"
   "Fangfire"
   "Soulimar"
   "CrispyTacoz"])

(def spring-power-ranks
  ["X"
   "Trevonte"
   "DSS"
   "Sean"
   "Silent Spectre"
   "Nitro"
   "Jeepy"
   "Rice"
   "Twich"
   "Scourge"
   "Phancy"
   "CrispyTacoz"
   "AD"
   "Bandt"
   "Hitaku"])

(def currently-ranked-players spring-power-ranks)
(def previously-ranked-players
  (vec (difference (set (concat january-power-ranks
                                february-power-ranks
                                march-power-ranks))
                   (set currently-ranked-players))))
