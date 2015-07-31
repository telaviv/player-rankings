(ns player-rankings.logic.tournament-constants
  (:require [clojure.set :refer [difference]]))

(def tournament-urls
  [
   "http://showdowngg.challonge.com/comeonandban2singles"
   "http://showdowngg.challonge.com/comeonandban3singles"
   "http://showdowngg.challonge.com/comeonandban4singles"
   "http://showdowngg.challonge.com/comeonandban5singles"
   "http://showdowngg.challonge.com/comeonandban6singles"
   "http://showdowngg.challonge.com/comeonandban7singles"
   "http://showdowngg.challonge.com/comeonandban8singles"
   "http://showdowngg.challonge.com/comeonandban9singles"
   "http://showdowngg.challonge.com/comeonandban10singles"
   "http://showdowngg.challonge.com/comeonandban11singles"
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
   "http://showdowngg.challonge.com/comeonandban23singles"
   "http://showdowngg.challonge.com/comeonandban24singles"
   "http://showdowngg.challonge.com/comeonandban25singles"
   "http://showdowngg.challonge.com/comeonandban26singles"
   "http://challonge.com/MADEsmashbi4singles"
   "http://challonge.com/MADE5Singles"
   "http://challonge.com/MADE6Singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/MADE8Singles"
   "http://challonge.com/MADE9Singles"
   "http://challonge.com/MADE10Singles"
   "http://challonge.com/MADE11Singles"
   "http://challonge.com/MADE12Singles"
   "http://challonge.com/BMLC1"
   "http://challonge.com/BMLC2"
   "http://challonge.com/BMLC3"
   "http://challonge.com/BMLC4"
   "http://challonge.com/BMLC5"
   "http://challonge.com/BMLC6"
   "http://challonge.com/BMLC7"
   "http://challonge.com/BMLC8"
   "http://challonge.com/BMLH1"
   "http://challonge.com/BMLH2"
   "http://challonge.com/BMLH3"
   "http://challonge.com/BMLH4"
   "http://challonge.com/BMLH5"
   "http://challonge.com/BMLH6"
   "http://challonge.com/BMOff1"
   "http://challonge.com/BMOff2"
   "http://scusmash.challonge.com/WW10S4"
   "http://scusmash.challonge.com/WW11S4"
   "http://scusmash.challonge.com/WW13_4Singles"
   "http://scusmash.challonge.com/WW14_4SINGLES"
   "http://scusmash.challonge.com/wwFinale_smash4"
   "http://capitolfightdistrict.challonge.com/cfdandgplswu"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://capitolfightdistrict.challonge.com/hn1tvh5t"
   "http://capitolfightdistrict.challonge.com/icrrkb29"
   "http://capitolfightdistrict.challonge.com/1vzpjxd1"
   "http://capitolfightdistrict.challonge.com/oojlvpxf"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://capitolfightdistrict.challonge.com/GSLatBHD"
   "http://capitolfightdistrict.challonge.com/alubwcl7"
   "http://capitolfightdistrict.challonge.com/fd8j4kag"
   "http://capitolfightdistrict.challonge.com/SL7"
   "http://capitolfightdistrict.challonge.com/SL8WU"
   "http://capitolfightdistrict.challonge.com/GSL9WU"
   "http://capitolfightdistrict.challonge.com/GSL10WU"
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
   "http://challonge.com/ghostdc1smash4singles"
   "http://challonge.com/ghostdc2smash4singles"
   "http://challonge.com/ghostdc3smash4singles"
   "http://challonge.com/ghostdc4smash4singles"
   "http://challonge.com/ghostdc5smash4singles"
   "http://challonge.com/ghostdc6smash4singles"
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
   "http://bko.challonge.com/OW3S4"
   "http://bko.challonge.com/OW4S"
   "http://bko.challonge.com/OW54"
   "http://bko.challonge.com/OW64"
   "http://bko.challonge.com/OW74"
   "http://challonge.com/blu_421"
   "http://challonge.com/Blu42_Jun2"
   "http://challonge.com/Blu421234"
   "http://challonge.com/blu42w8"
   "http://challonge.com/bsgdhfdshdssdhfsdh"
   "http://challonge.com/blu429"
   "http://challonge.com/afhsjfhbsljfhbdksahfbaskjf"
   "http://challonge.com/Blu42smashmass"
   "http://challonge.com/blu4211"
   "http://challonge.com/sm4shbruhsep2"
   "http://challonge.com/Simplysmashing1"
   ])


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
   "http://challonge.com/MADEsmashbi4singles"
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

(def test-urls summer-power-ranks-urls)


(def team-names
  [
   "sky raiders"
   "1up"
   "nme"
   "bask"
   "pho"
   "bko"
   "made"
   "8bit"
   "8 bit"
   "swarm"
   "uc"
   "fyt"
   "os"
   "fonc"
   "giga"
   ])

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
   ["Arda" "Ishiey"]
   ["Arda" "A"]
   ["Arda" "Adriel"]
   ["Arda" "Myro"]
   ["Arda" "AA"]
   ["Arda" "Ish"]
   ["ChosenL" "Chosen_L"]
   ["CrispyTacoz" "Tacoz"]
   ["z" "OS/z"]
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
   ["OS Thee.O.P" "OS | TheeOP"]
   ["Focast" "Foucast"]
   ["Chinito" "Chihito"]
   ["Jeepysol" "Jeepy"]
   ["My Jeans" "60 Scarabs"]
   ["A Stray Cat" "Stray"]
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
