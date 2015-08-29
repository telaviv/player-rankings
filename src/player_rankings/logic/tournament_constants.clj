
(ns player-rankings.logic.tournament-constants
  (:require [clojure.set :refer [difference]]))

(def the-foundry
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
   "http://showdowngg.challonge.com/comeonandban27"
   "http://showdowngg.challonge.com/comeonandban28"
   "http://showdowngg.challonge.com/comeonandban29"
   ])

(def made
  [
   "http://challonge.com/MADEsmashbi4singles"
   "http://challonge.com/MADE5Singles"
   "http://challonge.com/MADE6Singles"
   "http://challonge.com/MADE7Singles"
   "http://challonge.com/MADE8Singles"
   "http://challonge.com/MADE9Singles"
   "http://challonge.com/MADE10Singles"
   "http://challonge.com/MADE11Singles"
   "http://challonge.com/MADE12Singles"
   "http://challonge.com/MADE13Singles"
   "http://challonge.com/MADE14Singles"
   ])

(def big-mamas-little-cup
  [
   "http://challonge.com/BMLC1"
   "http://challonge.com/BMLC2"
   "http://challonge.com/BMLC3"
   "http://challonge.com/BMLC4"
   "http://challonge.com/BMLC5"
   "http://challonge.com/BMLC6"
   "http://challonge.com/BMLC7"
   "http://challonge.com/BMLC8"
   ])

(def big-mamas-little-house
  [
   "http://challonge.com/BMLH1"
   "http://challonge.com/BMLH2"
   "http://challonge.com/BMLH3"
   "http://challonge.com/BMLH4"
   "http://challonge.com/BMLH5"
   "http://challonge.com/BMLH6"
   ])

(def big-mamas-off-season
  [
   "http://challonge.com/BMOff1"
   "http://challonge.com/BMOff2"
   "http://challonge.com/BMOff3"
   "http://challonge.com/BMOS4"
   "http://challonge.com/BMOS5"
   "http://challonge.com/BMOS6"
   ])

(def wombo-wednesdays
  [
   "http://scusmash.challonge.com/WW10S4"
   "http://scusmash.challonge.com/WW11S4"
   "http://scusmash.challonge.com/WW13_4Singles"
   "http://scusmash.challonge.com/WW14_4SINGLES"
   "http://scusmash.challonge.com/wwFinale_smash4"
   ])

(def gamerz-smash-labs
  [
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
   "http://capitolfightdistrict.challonge.com/GSL11WU"
   "http://capitolfightdistrict.challonge.com/GSL12WU"
   "http://capitolfightdistrict.challonge.com/GSL13WU"
   "http://capitolfightdistrict.challonge.com/GSL14ABD"
   ])

(def ghost-at-dc
  [
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
   "http://challonge.com/ghostdc7smash4singles"
   "http://challonge.com/ghostdc9smash4singles"
   "http://challonge.com/GHOST10Smash4Singles"
   "http://challonge.com/ghostdc11smash4s"
   ])

(def ncr2015
  [
   "http://ncr2015.challonge.com/SSB41"
   "http://ncr2015.challonge.com/SSB42"
   "http://ncr2015.challonge.com/SSB43"
   "http://ncr2015.challonge.com/SSB44"
   "http://ncr2015.challonge.com/SSB45"
   "http://ncr2015.challonge.com/SSB46"
   "http://ncr2015.challonge.com/SSB47"
   "http://ncr2015.challonge.com/SSB48"
   "http://ncr2015.challonge.com/SSB416"
   ])

(def blu42-weekly
  [
   "http://challonge.com/blu_421"
   "http://challonge.com/Blu42_Jun2"
   "http://challonge.com/Blu421234"
   "http://challonge.com/blu42w8"
   "http://challonge.com/bsgdhfdshdssdhfsdh"
   "http://challonge.com/blu429"
   "http://challonge.com/afhsjfhbsljfhbdksahfbaskjf"
   "http://challonge.com/blu4211"
   "http://challonge.com/Blu42smash12"
   ])

(def blu42-smash-mass
  [
   "http://challonge.com/Blu42smashmass"
   ])

(def versus
  [
   "http://challonge.com/sm4shbruhsep1"
   "http://challonge.com/sm4shbruhsep2"
   "http://challonge.com/sm4shbruhsep3"
   "http://challonge.com/sm4shsep4"
   "http://challonge.com/sm4shsep5"
   "http://challonge.com/sm4shep6"
   "http://challonge.com/sm4shep7"
   "http://challonge.com/sm4shep8"
   "http://challonge.com/sm4shep9"
   ])


(def smash-odyssey
  [
   "http://bko.challonge.com/SO34"
   "http://bko.challonge.com/SO44"
   "http://bko.challonge.com/SO5M"
   "http://bko.challonge.com/OW3S4"
   "http://bko.challonge.com/OW4S"
   "http://bko.challonge.com/OW54"
   "http://bko.challonge.com/OW64"
   "http://bko.challonge.com/OW74"
   "http://bko.challonge.com/OW84"
   "http://bko.challonge.com/OW94"
   ])

(def the-2-stock-and-the-handshake
  [
   "http://challonge.com/thehandshake4"
   "http://challonge.com/thehandshake5"
   ])

(def teke-tourney
  [
   "http://challonge.com/teketourney"
   "http://challonge.com/Teke3"
   "http://challonge.com/Teke4"
   "http://challonge.com/Teke5"
   ])


(def miscellaneous-tournaments
  [
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://challonge.com/minibosssmash4singles2"
   "http://bko.challonge.com/BMS4S"
   "http://challonge.com/Simplysmashing1"
   ])

(def tournament-urls
  (concat the-foundry
          made
          big-mamas-little-cup
          big-mamas-little-house
          big-mamas-off-season
          wombo-wednesdays
          gamerz-smash-labs
          ghost-at-dc
          ncr2015
          blu42-weekly
          blu42-smash-mass
          versus
          smash-odyssey
          the-2-stock-and-the-handshake
          teke-tourney
          miscellaneous-tournaments))

(def test-urls
  [
   "http://showdowngg.challonge.com/comeonandban4singles"
   "http://showdowngg.challonge.com/comeonandban27"
   ])


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
   "gpl"
   "ct"
   "blu"
   "aether"
   "trnp"
   "ist"
   "cog"
   "sin"
   "4b"
   "koq"
   "beast"
   "dmg"
   ])

(def aliases
  [["Crow" "Chaos Crow" "Maleficent"]
   ["Glith" "glith10"]
   ["QT | K4rma" "QT.k4rma"]
   ["8BIT | Zepplin" "Zeppelin" "Zepland"]
   ["Mijo" "Mijo FUEGO"]
   ["Mr. Javi" "Mr Jav"]
   ["SKS" "watislyfe" "SKS aka Watislyfe"]
   ["NME | Nanerz" "Cynthia"]
   ["Rickshaw" "NinjaRlink" "Richshaw"]
   ["MF Space" "Space" "MF"]
   ["SirDaniel" "Sir"]
   ["Shia Lepuff" "Demi Lovato" "Shia La Puff" "ShaiLapuff"]
   ["Pulse" "RH | Old man Pulse"]
   ["8BIT | Blank" "Blankey69" "8BIT | Trojan"]
   ["BaNdt" "ARaNdomVillager"]
   ["OS | basedGO64" "OS/ basedgo64" "OS/.GO64" "GO"]
   ["Arikie" "MS | Shadow" "4B_Arikie" "Arike"]
   ["GShark" "Leffen_Shark" "G-Shark"]
   ["Arda" "Ishiey" "A" "Adriel" "AA" "Ish" "Im The Best Wolf"]
   ["ChosenL" "Chosen_L" "ChosenL*"]
   ["CrispyTacoz" "Tacoz" "TRNP | CrispyTacoz *"]
   ["z" "OS/z"]
   ["FONC | GPL Chye" "FONC | KOCI"]
   ["FONC | Fist o cuffs" "FistOCuffs" "FONC | Fist'O'cuffs"]
   ["Violet" "Andrew Le"]
   ["Scourge" "Summus"]
   ["Kronos2560" "Kronos"]
   ["Hitaku" "Hitaku Back Sunday"]
   ["Boba Tapioca" "Boba"]
   ["UC | DSS" "@UC_DSS"]
   ["Beast" "Daimyes" "Jimber Jangers"]
   ["MisterQ" "MrQ" "Mr. Q"]
   ["QT | Haystack" "QT|Haystax" "QT|Dadstax"]
   ["Chaos Pro" "Sm4sh Mango aka Chaos Pro" "Smash 4 Mango aka ChaosPro" "Rocky Balboa"]
   ["BKO | Choknater" "BKO | Chokenator"]
   ["Rice" "Rice-kun" "Dark Rice"]
   ["Andy Sauro" "Andy_Sauro" "Andy" "Andy The Albatross"]
   ["Shin" "Ph~ck Shin"]
   ["OS Thee.O.P" "OS | TheeOP" "OS | thee.O.P."]
   ["Focast" "Foucast"]
   ["Chinito" "Chihito"]
   ["Jeepysol" "Jeepy"]
   ["My Jeans" "60 Scarabs"]
   ["A Stray Cat" "Stray" "StrayCat" "A Stay Cat"]
   ["Trex Destiny" "T-Rex Destiny"]
   ["AwesomeTheSauce" "AwesomeSauce"]
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
