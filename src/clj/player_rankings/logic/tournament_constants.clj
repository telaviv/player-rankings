(ns player-rankings.logic.tournament-constants
  (:require [clojure.set :refer [difference]]
            [clojure.string :as string]
            [player-rankings.parsers.smashgg :as smashgg]
            [player-rankings.parsers.challonge :as challonge]
            [clj-time.core :as t]))

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
   "http://showdowngg.challonge.com/comeonandban30"
   "http://showdowngg.challonge.com/comeonandban31"
   "https://smash.gg/tournament/come-on-and-ban-32"
   "https://smash.gg/tournament/come-on-and-ban-33"
   "http://showdowngg.challonge.com/comeonandban34"
   "http://showdowngg.challonge.com/comeonandban35"
   "http://showdowngg.challonge.com/comeonandban36"
   "http://showdowngg.challonge.com/comeonandban38"
   "http://showdowngg.challonge.com/comeonandban39"
   "http://showdowngg.challonge.com/comeonandban40"
   "http://showdowngg.challonge.com/comeonandban41"
   "http://showdowngg.challonge.com/comeonandban42"
   "http://showdowngg.challonge.com/comeonandban43"
   "http://showdowngg.challonge.com/comeonandban44"
   "http://showdowngg.challonge.com/comeonandban45"
   "http://showdowngg.challonge.com/comeonandban47"
   "http://showdowngg.challonge.com/comeonandban48"
   "http://showdowngg.challonge.com/comeonandban49"
   "http://showdowngg.challonge.com/comeonandban50"
   "http://showdowngg.challonge.com/comeonandban51"
   "http://showdowngg.challonge.com/comeonandban52"
   "http://showdowngg.challonge.com/comeonandban53"
   "http://showdowngg.challonge.com/comeonandban54"
   "http://showdowngg.challonge.com/comeonandban55"
   "http://showdowngg.challonge.com/comeonandban56"
   "http://showdowngg.challonge.com/comeonandban57"
   "http://showdowngg.challonge.com/comeonandban58"
   "http://showdowngg.challonge.com/comeonandban59"
   "http://showdowngg.challonge.com/comeonandban60"
   "http://showdowngg.challonge.com/comeonandban61"
   "http://showdowngg.challonge.com/comeonandban62"
   "http://showdowngg.challonge.com/comeonandban63"
   "http://showdowngg.challonge.com/comeonandban64"
   ])

(def smash-of-the-titans
  [
   "https://smash.gg/tournament/smash-of-the-titans-3"
   "https://smash.gg/tournament/smash-of-the-titans-2-1"
   "https://smash.gg/tournament/smash-of-the-titans-3-1"
   "https://smash.gg/tournament/smash-of-the-titans-4"
   "https://smash.gg/tournament/smash-of-the-titans-5"
   "https://smash.gg/tournament/smash-of-the-titans-6"
   "https://smash.gg/tournament/smash-of-the-titans-7"
   "https://smash.gg/tournament/smash-of-the-titans-8"
   "https://smash.gg/tournament/smash-of-the-titans-9"
   "https://smash.gg/tournament/smash-of-the-titans-10"
   "https://smash.gg/tournament/smash-of-the-titans-11"
   "https://smash.gg/tournament/smash-of-the-titans-12"
   "https://smash.gg/tournament/smash-of-the-titans-13"
   "https://smash.gg/tournament/smash-of-the-titans-21"
   "https://smash.gg/tournament/smash-of-the-titans-22"
   "https://smash.gg/tournament/smash-of-the-titans-23"
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
   "http://challonge.com/SmashMassXMADESingles"
   "http://challonge.com/MADESingles15"
   "http://challonge.com/MADEFinalSingles"
   "http://challonge.com/MADESmash416Singles"
   "http://challonge.com/made17singles"
   "http://challonge.com/smash18singles"
   "http://challonge.com/smash19singles"
   "http://challonge.com/MADE20singles"
   "http://challonge.com/MADE21singles"
   "http://challonge.com/MADE22Singles"
   "http://challonge.com/MADE23Singles"
   "http://challonge.com/MADE24Singles"
   "http://challonge.com/Sm4shmadesingles"
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
   "http://challonge.com/BMLHTR"
   "http://challonge.com/BMLH42"
   "http://srs.challonge.com/BMLH43"
   "http://srs.challonge.com/BMLH44"
   "http://srs.challonge.com/BMLH46"
   "http://srs.challonge.com/BMLH47"
   "http://srs.challonge.com/BMLH48"
   "http://srs.challonge.com/BMLH49"
   "http://srs.challonge.com/BMLH50"
   "http://srs.challonge.com/BMLH51"
   "http://srs.challonge.com/BMLH52"
   "http://srs.challonge.com/BMLH53"
   "http://srs.challonge.com/BMLH54"
   "http://srs.challonge.com/BMLH55"
   "http://srs.challonge.com/bmlh56"
   "http://srs.challonge.com/BMLH57"
   "http://srs.challonge.com/bmlh58"
   "http://srs.challonge.com/BMLH59"
   "http://srs.challonge.com/bmlh60"
   ])

(def big-mamas-off-season
  [
   "http://challonge.com/BMOff1"
   "http://challonge.com/BMOff2"
   "http://challonge.com/BMOff3"
   "http://challonge.com/BMOS4"
   "http://challonge.com/BMOS5"
   "http://challonge.com/BMOS6"
   "http://challonge.com/BMOS7"
   "http://challonge.com/BMOS8"
   "http://challonge.com/BMOS9"
   "http://challonge.com/BMOS10"
   "http://challonge.com/BMOS11"
   "http://challonge.com/BMOS12"
   "http://challonge.com/BMOS13"
   "http://challonge.com/BMOS14"
   "http://challonge.com/bmos15"
   "http://challonge.com/BMOS16"
   "http://challonge.com/BMOS17"
   "http://challonge.com/BMOS18"
   ])

(def wombo-wednesdays
  [
   "http://scusmash.challonge.com/WW10S4"
   "http://scusmash.challonge.com/WW11S4"
   "http://scusmash.challonge.com/WW13_4Singles"
   "http://scusmash.challonge.com/WW14_4SINGLES"
   "http://scusmash.challonge.com/wwFinale_smash4"
   "http://scusmash.challonge.com/WW16S4singles"
   "http://scusmash.challonge.com/WW17s4singles"
   "http://scusmash.challonge.com/WWS418"
   "http://scusmash.challonge.com/WW19s4singles"
   "http://scusmash.challonge.com/WW20S4singles"
   "http://scusmash.challonge.com/WW21S4"
   "http://scusmash.challonge.com/ww21s4singles"
   "http://scusmash.challonge.com/ww23s4s"
   "http://scusmash.challonge.com/WW24S4singles"
   "http://scusmash.challonge.com/ww25s4s"
   "http://scusmash.challonge.com/ww26s4sings"
   "http://scusmash.challonge.com/ww27s4s"
   "http://scusmash.challonge.com/ww28s4s"
   "http://scusmash.challonge.com/WW29s4s"
   "http://scusmash.challonge.com/ww30s4"
   "https://smash.gg/tournament/wombo-wednesday-31"
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
   "http://capitolfightdistrict.challonge.com/GSL15CES"
   "http://capitolfightdistrict.challonge.com/GSL16HBRS"
   "http://capitolfightdistrict.challonge.com/GSL17WU"
   "http://capitolfightdistrict.challonge.com/GSL18"
   "http://capitolfightdistrict.challonge.com/GSL19S"
   "http://capitolfightdistrict.challonge.com/GttES"
   "http://capitolfightdistrict.challonge.com/GSL21S"
   "http://capitolfightdistrict.challonge.com/GSL22GRD"
   "http://capitolfightdistrict.challonge.com/prebloodmoonS"
   "http://capitolfightdistrict.challonge.com/GSL24S"
   "http://capitolfightdistrict.challonge.com/GSL25S"
   "http://capitolfightdistrict.challonge.com/GSL26S"
   "http://capitolfightdistrict.challonge.com/GSL27S"
   "http://capitolfightdistrict.challonge.com/GSL28S"
   "http://capitolfightdistrict.challonge.com/GSL29S"
   "http://capitolfightdistrict.challonge.com/GSL30S"
   "http://capitolfightdistrict.challonge.com/GSL31S"
   "http://capitolfightdistrict.challonge.com/GSL32NBS"
   "http://capitolfightdistrict.challonge.com/GSL33S"
   "http://capitolfightdistrict.challonge.com/GSL34S"
   "http://capitolfightdistrict.challonge.com/GSL35WIIUS"
   "http://capitolfightdistrict.challonge.com/GSL36WUS"
   "http://capitolfightdistrict.challonge.com/GSL37WUS"
   "http://capitolfightdistrict.challonge.com/GSL38WUS"
   "http://capitolfightdistrict.challonge.com/GSL39WUS"
   "http://capitolfightdistrict.challonge.com/GGSL40WUS"
   "http://capitolfightdistrict.challonge.com/GSL41WUS"
   ])

(def gamerz-underground
  [
   "http://challonge.com/GSU1"
   "http://challonge.com/GZUuno"
   "http://challonge.com/GUDWUS"
   "http://challonge.com/GUTWUS"
   "http://challonge.com/GUCWYWS"
   "http://challonge.com/GUSS"
   "http://challonge.com/GZUOS"
   "http://challonge.com/GZUDS"
   "http://challonge.com/GZUOSs"
   "http://challonge.com/GUCS"
   "http://challonge.com/GZUNS"
   "http://challonge.com/GZUSS"
   "http://challonge.com/GZUQS"
   "http://gzugg.challonge.com/GZUDS"
   "http://gzugg.challonge.com/GZUDSSS"
   ])

(def gamerz-rebooted
  [
   "http://gzugg.challonge.com/gamerzrebooted1S"
   "http://gzugg.challonge.com/gamerzrebooted2S"
   "http://gzugg.challonge.com/gamerzrebooted3S"
   "http://gzugg.challonge.com/gamerzrebooted4s"
   "http://gzugg.challonge.com/gamerzrebooted5s"
   "http://gzugg.challonge.com/gamerzrebooted6s"
   "http://gzugg.challonge.com/gamerzrebooted7S"
   "http://gzugg.challonge.com/gamerzrebooted8S"
   "http://gzugg.challonge.com/gamerzrebooted9s"
   "http://gzugg.challonge.com/gamerzrebooted10s"
   "http://gzugg.challonge.com/gamerzrebooted11S"
   "http://gzugg.challonge.com/gamerzrebooted12S"
   "http://gzugg.challonge.com/gamerzrebooted13s"
   "http://gzugg.challonge.com/gamerzrebooted14s"
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
   "http://challonge.com/ghostdc12smash4singles"
   "http://challonge.com/ghostarcadiansmash4singles"
   "http://challonge.com/ghostdc13smash4singles"
   "http://challonge.com/ghostdc14smas4"
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
   "http://challonge.com/nvaxrbp3"
   "http://challonge.com/blu42w8"
   "http://challonge.com/bsgdhfdshdssdhfsdh"
   "http://challonge.com/blu429"
   "http://challonge.com/Smash4Blu42"
   "http://challonge.com/afhsjfhbsljfhbdksahfbaskjf"
   "http://challonge.com/blu4211"
   "http://challonge.com/Blu42smash12"
   ])

(def blu42-smash-mass
  [
   "http://challonge.com/Blu42smashmass"
   ])

(def smash-boba-and-tea
  [
   "http://challonge.com/smashbobaandteasingles"
   "http://challonge.com/smashbobaandtea3singles"
   "http://challonge.com/smashbobaandtea5singles"
   ])

(def tea-rex
  [
   "http://challonge.com/teabobasmash1"
   "http://challonge.com/smashtrex2"
   "http://challonge.com/smashtrex3"
   "http://challonge.com/Sm4shtearex4"
   "http://challonge.com/smashtearex5"
   "http://challonge.com/Smashtearex6"
   "http://challonge.com/smashtearex7"
   "http://challonge.com/smashtearex8"
   "http://challonge.com/smashtearex9"
   "http://challonge.com/smashtearex10"
   "http://challonge.com/smashtearex11"
   "http://challonge.com/smashtearex12"
   "http://challonge.com/smashtearex13"
   "http://challonge.com/smashtearex14"
   ])

(def smash-at-ronin
  [
   "http://challonge.com/ESR_1"
   "http://challonge.com/ESR_2"
   "http://challonge.com/ESR_3"
   "http://challonge.com/ESR4"
   ])

(def share-stock-saturdays
  [
   "http://challonge.com/sssep5"
   "http://challonge.com/Sssep7"
   "http://challonge.com/Sss9"
   "http://challonge.com/sssep10"
   "http://challonge.com/singlessss11"
   ])

(def back-2-basics
  [
   "http://challonge.com/Back2Basics"
   "http://challonge.com/Back2Basics2"
   "http://challonge.com/B2B3"
   "http://challonge.com/back2basics4"
   "http://challonge.com/jje1dcw6"
   "http://challonge.com/g4yiyy0h"
   "http://challonge.com/h8kp5gf6"
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
   "http://challonge.com/sm4shep10"
   "http://challonge.com/sm4sh11"
   {:url "http://brackets.godlikecombo.com/#!/versus_smash4_singles_12"
    :date (t/date-time 2015 9 14)
    :title "Versus Revival - Sm4sh Singles ep 12"}
   {:url "http://brackets.godlikecombo.com/#!/versus_smash_singles_ep13"
    :date (t/date-time 2015 9 21)
    :title "Versus Revival - Sm4sh Singles ep 13"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh14"
    :date (t/date-time 2015 10 5)
    :title "Versus Revival - Sm4sh Singles ep 14"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh15"
    :date (t/date-time 2015 10 19)
    :title "Versus Revival - Sm4sh Singles ep 15"}
   {:url "http://brackets.godlikecombo.com/#!/sm4shep16"
    :date (t/date-time 2015 10 26)
    :title "Versus Revival - Sm4sh Singles ep 16"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh17"
    :date (t/date-time 2015 11 2)
    :title "Versus Revival - Sm4sh Singles ep 17"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh18"
    :date (t/date-time 2015 11 9)
    :title "Versus Revival - Sm4sh Singles ep 18"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh19"
    :date (t/date-time 2015 11 16)
    :title "Versus Revival - Sm4sh Singles ep 19"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh20"
    :date (t/date-time 2015 11 23)
    :title "Versus Revival - Sm4sh Singles ep 20"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh21"
    :date (t/date-time 2015 11 30)
    :title "Versus Revival - Sm4sh Singles ep 21"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh22"
    :date (t/date-time 2015 12 7)
    :title "Versus Revival - Sm4sh Singles ep 22"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh23"
    :date (t/date-time 2016 1 11)
    :title "Versus Revival - Sm4sh Singles ep 23"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh24"
    :date (t/date-time 2016 1 25)
    :title "Versus Revival - Sm4sh Singles ep 24"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh25"
    :date (t/date-time 2016 2 2)
    :title "Versus Revival - Sm4sh Singles ep 25"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh26"
    :date (t/date-time 2016 2 9)
    :title "Versus Revival - Sm4sh Singles ep 26"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh27"
    :date (t/date-time 2016 2 16)
    :title "Versus Revival - Sm4sh Singles ep 27"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh28"
    :date (t/date-time 2016 2 23)
    :title "Versus Revival - Sm4sh Singles ep 28"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh29"
    :date (t/date-time 2016 4 25)
    :title "Versus Revival ep 29"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh30"
    :date (t/date-time 2016 5 2)
    :title "Versus Revival ep 30"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh31"
    :date (t/date-time 2016 5 2)
    :title "Versus Revival ep 31"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh33"
    :date (t/date-time 2016 5 23)
    :title "Versus Revival ep 33"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh34"
    :date (t/date-time 2016 5 30)
    :title "Versus Revival ep 34"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh35"
    :date (t/date-time 2016 6 6)
    :title "Versus Revival ep 35"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh36"
    :date (t/date-time 2016 6 13)
    :title "Versus Revival ep 36"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh37"
    :date (t/date-time 2016 6 20)
    :title "Versus Revival ep 37"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh38"
    :date (t/date-time 2016 6 27)
    :title "Versus Revival ep 38"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh39"
    :date (t/date-time 2016 7 7)
    :title "Versus Revival ep 39"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh40"
    :date (t/date-time 2016 7 14)
    :title "Versus Revival ep 40"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh41"
    :date (t/date-time 2016 7 21)
    :title "Versus Revival ep 41"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh42"
    :date (t/date-time 2016 7 28)
    :title "Versus Revival ep 42"}
   {:url "http://brackets.godlikecombo.com/#!/sm4sh43"
    :date (t/date-time 2016 8 29)
    :title "Sayonara Versus - The Final Episode"}
   ])

(def rise-at-ssf
  [
   "http://challonge.com/risessfs3e1"
   "http://challonge.com/risessfs3e2"
   ])

(def casa-del-fuego
  [
   "http://challonge.com/CDF1singles"
   "http://challonge.com/CDF2"
   "http://challonge.com/casadefeugorevival"
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
   "http://bko.challonge.com/SO64"
   "http://bko.challonge.com/SO7S4"
   "http://bko.challonge.com/SO84"
   "http://bko.challonge.com/SO94"
   "http://bko.challonge.com/SO104"
   ])

(def the-2-stock-and-the-handshake
  [
   "http://challonge.com/2stocked"
   "http://challonge.com/thehandshake2"
   "http://challonge.com/thehandshake3"
   "http://challonge.com/thehandshake4"
   "http://challonge.com/thehandshake5"
   "http://challonge.com/thehandshake6"
   "http://challonge.com/thehandshake7"
   "http://challonge.com/thehandshake8"
   "http://challonge.com/thehandshake9"
   "http://challonge.com/thehandshake10"
   ])

(def teke-tourney
  [
   "http://challonge.com/teketourney"
   "http://challonge.com/Teke3"
   "http://challonge.com/Teke4"
   "http://challonge.com/Teke5"
   "http://challonge.com/Teke6"
   "http://challonge.com/Teke7"
   "http://challonge.com/Teke8"
   "http://challonge.com/Teke9"
   "http://challonge.com/Teke10"
   "http://challonge.com/Teke11Singles"
   ])

(def game-addiction
  [
   "http://challonge.com/GAddiction2"
   "http://challonge.com/GAddiction3"
   "http://challonge.com/Gaddiction4"
   "http://challonge.com/Gaddiction5"
   "http://challonge.com/GAddiction6"
   "http://challonge.com/gaddiction7"
   "http://challonge.com/gaddiction8"
   "http://challonge.com/Gaddiction9"
   "http://challonge.com/Gaddiction10"
   ])

(def stiikx
  [
   "http://challonge.com/stiikx3"
   "http://challonge.com/stiikx4"
   "http://challonge.com/stiikx5smash4sing"
   "http://challonge.com/stiikx6smash4sing"
   ])

(def pheonix-uprising
  [
   "http://challonge.com/phoenixuprising1"
   "http://challonge.com/phoenixuprising2"
   "http://challonge.com/phoenixuprising3"
   "http://challonge.com/phoenixuprising4"
   "http://challonge.com/phoenixuprising5"
   "http://challonge.com/phoenixuprising6"
   "http://challonge.com/phoenixuprising7"
   "http://challonge.com/phoenixuprising8"
   "http://challonge.com/phoenixuprising9"
   "http://challonge.com/phoenixuprising10"
   "http://challonge.com/phoenixuprising11"
   "http://challonge.com/phoenixuprising12"
   "http://challonge.com/phoenixuprising13"
   "http://challonge.com/phoenixuprising14"
   "http://challonge.com/phoenixuprising15"
   "http://challonge.com/phoenixuprising16"
   "http://challonge.com/phoenixuprising17"
   "http://challonge.com/phoenixuprising18"
   "http://challonge.com/phoenixuprising19"
   "http://challonge.com/phoenixuprising20"
   "http://challonge.com/phoenixuprising21"
   "http://challonge.com/phoenixuprising22"
   "http://challonge.com/phoenixuprising23"
   "http://challonge.com/phoenixuprising24"
   "http://challonge.com/phoenixuprising25"
   "http://challonge.com/phoenixuprising26"
   "http://challonge.com/phoenixuprising27"
   "http://challonge.com/phoenixuprising28"
   "http://challonge.com/phoenixuprising29"
   "http://challonge.com/phoenixuprising30"
   "http://challonge.com/phoenixuprising31"
   "http://challonge.com/phoenixuprising32"
   "http://challonge.com/phoenixuprising33"
   "http://challonge.com/phoenixuprising34"
   "http://challonge.com/phoenixuprising35"
   "http://challonge.com/phoenixuprising36"
   "http://challonge.com/phoenixuprising37"
   "http://challonge.com/phoenixuprising38"
   "http://challonge.com/phoenixuprising39"
   "http://challonge.com/phoenixuprising40"
   "http://challonge.com/phoenixuprising41"
   "http://challonge.com/phoenixuprising42"
   "http://challonge.com/phoenixuprising43"
   "http://challonge.com/phoenixuprising44"
   "http://challonge.com/phoenixuprising45"
   "http://challonge.com/phoenixuprising46"
   "http://challonge.com/phoenixuprising47"
   "http://challonge.com/phoenixuprising48"
   "http://challonge.com/phoenixuprising49"
   "http://challonge.com/phoenixuprising50"
   "http://challonge.com/phoenixuprising51"
   "http://challonge.com/phoenixuprising52"
   "http://challonge.com/phoenixuprising53"
   "http://challonge.com/phoenixuprising54"
   "http://challonge.com/phoenixuprising55"
   "http://challonge.com/phoenixuprising56"
   "http://challonge.com/phoenixuprising57"
   "http://challonge.com/phoenixuprising58"
   "http://challonge.com/phoenixuprising59"
   "http://challonge.com/phoenixuprising60"
   "http://challonge.com/phoenixuprising61"
   "http://challonge.com/phoenixuprising62"
   "http://challonge.com/phoenixuprising63"
   "http://challonge.com/phoenixuprising64"
   "http://challonge.com/phoenixuprising65"
   "http://challonge.com/phoenixuprising66"
   "http://challonge.com/phoenixuprising67"
   "http://challonge.com/phoenixuprising68"
   "http://challonge.com/phoenixuprising69"
   "http://challonge.com/phoenixuprising70"
   "http://challonge.com/phoenixuprising79"
   ])

(def pheonix-awakening
  [
   "http://challonge.com/phoenixawakeningsingles"
   "http://challonge.com/phoenixawakening2singles"
   ])

(def super-south-bay-sundays
  [
   "http://ssbs.challonge.com/S4S1"
   "http://ssbs.challonge.com/sm4sheaster"
   "http://ssbs.challonge.com/SSBS5_S4Singles"
   "http://ssbs.challonge.com/SSBS6S4S"
   "http://ssbs.challonge.com/SSBS7_4Singles"
   "http://ssbs.challonge.com/SSBS8S4singles"
   "http://ssbs.challonge.com/SSBS9s4singles"
   "http://ssbs.challonge.com/SSBS10S4singles"
   "http://ssbs.challonge.com/ssbs114"
   "http://ssbs.challonge.com/SSBS12S4singles"
   "http://ssbs.challonge.com/ssbs13sm4shsingles"
   "http://ssbs.challonge.com/SSBS14smash4Singles"
   "http://ssbs.challonge.com/ssbs15s4s"
   "http://ssbs.challonge.com/ssbs16s4s"
   "http://ssbs.challonge.com/ssbs17s4s"
   "https://smash.gg/tournament/super-south-bay-sunday-18-who-are-you-people-edition"
   "https://smash.gg/tournament/super-south-bay-sunday-19"
   "https://smash.gg/tournament/super-south-bay-sunday-21"
   ])

(def simply-smashing
  ["http://challonge.com/Simplysmashing1"
   "http://challonge.com/Ssmashing2"
   "http://challonge.com/Ssmashing3"
   "http://challonge.com/SSmashing4"
   "http://challonge.com/SSmashing5"
   "http://challonge.com/SSmashing6"
   "http://challonge.com/ssmashing7"
   "http://challonge.com/SSmashing8"
   "http://challonge.com/Ssmashing9"
   "http://challonge.com/SSmashing11"
   "http://challonge.com/SSmashing12"
   "http://challonge.com/SSmashing13"
   "http://challonge.com/Ssmashing14"
   "http://challonge.com/Ssmashing15"
   "http://challonge.com/SSmashing16"
   "http://challonge.com/ssmashing17"
   "http://challonge.com/SSmashing18"
   "http://challonge.com/SSmashing19"
   "http://challonge.com/Ssmashing20"
   "http://challonge.com/Ssmashing21"
   "http://challonge.com/Ssmashing22"
   "http://challonge.com/Ssmashing23"
   "http://challonge.com/Ssmashing24"
   "http://challonge.com/Ssmashing25"
   ])

(def fair-fights
  [
   "http://terrashockgg.challonge.com/FF_WiiU"
   "http://terrashockgg.challonge.com/FF2_WiiU"
   "http://terrashockgg.challonge.com/FF3_WiiU"
   "http://terrashockgg.challonge.com/FF4_WiiU"
   "http://terrashockgg.challonge.com/FF5_WiiU"
   "http://terrashockgg.challonge.com/FF6_WiiU"
   "http://terrashockgg.challonge.com/FF7_WiiU"
   "http://terrashockgg.challonge.com/FF8_WiiU"
   "http://terrashockgg.challonge.com/FF9_WiiU"
   "http://terrashockgg.challonge.com/FF10_WiiU"
   "http://terrashockgg.challonge.com/FF11_WiiU"
   "http://terrashockgg.challonge.com/FF12_WiiU"
   "http://terrashockgg.challonge.com/FF13_WiiU"
   ])

(def no-fair
  [
   "http://challonge.com/NoFair1S"
   "http://challonge.com/NoFair2S"
   ])


(def the-forge
  ["http://challonge.com/TheForge1"
   "http://challonge.com/HeatII"
   "http://challonge.com/Forge3"
   "http://challonge.com/Heat4"
   "http://challonge.com/ForgeV"
   ])

(def smash-at-kin
  [
   "http://smashkin.challonge.com/smashkin1"
   "http://challonge.com/zsv9hsmy"
   "http://challonge.com/74jzzh0c"
   "http://challonge.com/Smashkin4dontgethit"
   "http://challonge.com/SmashKin5"
   "http://challonge.com/SmashKin6"
   "http://challonge.com/SaK7"
   "http://challonge.com/Smashkin8"
   "http://challonge.com/SmashKin9"
   "http://challonge.com/sak10"
   "http://challonge.com/ESK_Singles"
   "http://challonge.com/ESK12"
   "http://challonge.com/ESK_13"
   "http://challonge.com/ESK_14"
   "http://challonge.com/ESK_15"
   "http://challonge.com/ESK16"
   "http://challonge.com/ESK_17"
   "http://challonge.com/ESK_18"
   "http://challonge.com/ESK_19"
   "http://e1337gaming.challonge.com/ESK_21"
   "http://challonge.com/Singles22kin"
   "http://e1337gaming.challonge.com/ESK23"
   "http://e1337gaming.challonge.com/ESK24"
   ])

(def revival-of-kin
  [
   "http://challonge.com/RoKsingles1"
   "http://challonge.com/RoK2Singles"
   "http://challonge.com/ROK3singles"
   "http://challonge.com/ROK4singles"
   "http://challonge.com/ROK5singles"
   "http://challonge.com/ROK6singles"
   "http://challonge.com/ROK7singles"
   "http://challonge.com/ROK8singles"
   "http://challonge.com/ROK9singles"
   "http://challonge.com/ROK10singles"
   "http://challonge.com/ROK11singles"
   "http://challonge.com/ROK12singles"
   "http://challonge.com/ROK13singles"
   "http://challonge.com/ROK14singles"
   "http://challonge.com/ROK15singles"
   "http://challonge.com/ROK16singles"
   "http://challonge.com/ROK17singles"
   "http://challonge.com/ROK18singles"
   "http://challonge.com/rok25singles"
   ])


(def e1337ent-smash-open
  ["http://challonge.com/ESO_W1_2016"
   "http://challonge.com/ESO_W2"
   "http://challonge.com/ESO_W3"
   "http://challonge.com/ESO_W4"
   "http://challonge.com/ESO_W5"
   ])


(def smash-at-gg
  ["http://challonge.com/ESO_S1"
   "http://challonge.com/ESO_S2"
   "http://challonge.com/ESO_S3"
   "http://challonge.com/ESGT_3"
   "http://challonge.com/ESGT_4"
   "http://challonge.com/ESG_S4"
   "http://challonge.com/ESO_S5"
   "http://challonge.com/ESO_T4"
   "http://challonge.com/ESG_T5"
   "http://challonge.com/ESO_S6"
   "http://challonge.com/ESO_S7"
   "http://challonge.com/ESG_T8"
   "http://challonge.com/ESG_T9"
   "http://e1337gaming.challonge.com/ESG_T10"
   "http://e1337gaming.challonge.com/ESG_T11"
   "http://e1337gaming.challonge.com/ESO_Tuesday12"
   "http://e1337gaming.challonge.com/ESG_T13"
   "http://e1337gaming.challonge.com/ESO_T14"
   ])

(def eso-tuesdays
  [
   "http://challonge.com/ESO_T1"
   "http://challonge.com/ESO_T5"
   "http://challonge.com/ESO_Tues_2"
   "http://challonge.com/ESO_W6"
   ])

(def smash-for-tats
  [
   "http://challonge.com/SMASHFORTATS"
   ])

(def smash-at-berkeley
  ["http://challonge.com/bayo"
   "http://sab.challonge.com/sp16bw2_4"
   "http://sab.challonge.com/sp16bw3smash4"
   "http://sab.challonge.com/sp16bw4singles"
   "http://sab.challonge.com/fdsm4shsingles"
   "http://sab.challonge.com/bbb1sm4shsingles"
   "http://sab.challonge.com/bbb2sm4shsingles"
   "http://sab.challonge.com/bbb3sm4shsingles"
   ])

(def bair-necessities
  ["http://sab.challonge.com/BairNecessities1"])

(def did-that-just-happen
  ["http://challonge.com/dtjhsjsu04Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu05Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu07Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu09Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu10Smash4"
   "http://challonge.com/sjsu11_Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu12Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu13Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu14Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu15Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu16Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu17Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu18Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu19Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu21Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu22Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu23Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu24Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu25Smash4"
   "http://sslsmash.challonge.com/mdtjhsjsu26Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu27Smash4"
   "http://sslsmash.challonge.com/mdtjhsjsu28Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu29Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu30Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu31Smash4"
   "http://sslsmash.challonge.com/mdtjhsjsu32Smash4"
   "http://sslsmash.challonge.com/mdtjhsjsu34Smash4"
   ])

(def super-slug-fighters
  ["http://scfgc.challonge.com/superslugfighters_sm4sh"
   "http://scfgc.challonge.com/ssb4_top32_ssf2"])

(def fight-nights
  [
   "http://scfgc.challonge.com/ssb4_ranked_night_8"
   "http://scfgc.challonge.com/ssb4_ranked_night_12"
   "http://scfgc.challonge.com/ssb4_ranked_night_15"
   "http://scfgc.challonge.com/ssb4_ranked_night_16"
   "http://scfgc.challonge.com/ssb4_ranked_night_18"
   "http://scfgc.challonge.com/ssb4_ranked_night_21"
   "http://scfgc.challonge.com/ssb4_ranked_night_30"
   "http://scfgc.challonge.com/ssb4_ranked_night_33"
   "http://scfgc.challonge.com/ssb4_ranked_night_36"
   "http://scfgc.challonge.com/ssb4_ranked_night_38"
   "http://scfgc.challonge.com/ssbm_ranked_night_40"
   "http://scfgc.challonge.com/ssb4_ranked_night_42"
   ])

(def beta-blast
  [
   "http://challonge.com/betaboyz"
   "http://challonge.com/Betaz"
   "http://challonge.com/Betazs"
   "http://challonge.com/betablast4"
   "http://challonge.com/Betablast5"
   "http://challonge.com/Hddghutt"
   ])

(def solitude-smash
  [
   "http://challonge.com/GameNightsSSB4"
   "http://challonge.com/SoSsmash4"
   "http://challonge.com/SoS3smash4"
   "http://challonge.com/Smashshore3singles"
   ])

(def smash-4-cash
  [
   "http://cvs.challonge.com/smash4cash5"
   "http://cvs.challonge.com/smash4cash6"
   "http://cvs.challonge.com/smash4cash7smash4singles"
   "http://apstournaments.challonge.com/s4c8smash4"
   "http://apstournaments.challonge.com/s4c9smash4"
   ])

(def battleground-wii-u
  [
   "http://challonge.com/BGwiiU"
   "http://challonge.com/BGWiiU2"
   "http://challonge.com/BGWiiU3"
   "http://challonge.com/BGWiiU4"
   "http://challonge.com/BGWiiU5"
   "http://challonge.com/BGWiiU6"
   "http://challonge.com/BGWiiU7"
   "http://challonge.com/BGWiiU8"
   "http://challonge.com/BGWiiU9"
   "http://challonge.com/BGWiiU10"
   "http://challonge.com/BGWiiU11"
   "http://challonge.com/BGWiiU12"
   "http://challonge.com/BGWiiU13"
   "http://challonge.com/BGWiiU14"
   "http://challonge.com/BGWiiU15"
   "http://challonge.com/BGWiiU16"
   "http://challonge.com/BGWiiU17"
   "http://challonge.com/BGWiiU18"
   "http://challonge.com/BGWiiU19"
   "http://challonge.com/BGWiiU20"
   "http://challonge.com/BGWiiU21"
   "http://challonge.com/BGWiiU22"
   "http://challonge.com/BGWiiU23"
   "http://challonge.com/BGWiiU24"
   "http://challonge.com/BGWiiU025"
   "http://challonge.com/BGWiiU26"
   "http://challonge.com/BGWiiU27"
   "http://challonge.com/BGWiiU28"
   "http://challonge.com/BGWiiU29"
   "http://challonge.com/BGWiiU30"
   "http://challonge.com/BGWiiU31"
   "http://challonge.com/BGWiiU32"
   "http://challonge.com/BGWiiU33"
   "http://challonge.com/BGWiiU34"
   "http://challonge.com/BGWiiU35"
   "http://challonge.com/BGWiiU36"
   "http://challonge.com/BGWiiU37"
   "http://challonge.com/BGWiiU38"
   "http://challonge.com/BGWiiU39"
   "http://challonge.com/BGWiiU40"
   "http://challonge.com/BGWiiU41"
   "http://challonge.com/BGWiiU42"
   "http://challonge.com/BGWiiU43"
   "http://challonge.com/BGWiiU44"
   "http://challonge.com/BGWiiU45"
   ])

(def welcome-to-cowtown
  [
   "http://challonge.com/NorCow"
   "http://challonge.com/welcometocowtown2"
   "http://challonge.com/NorCow2"
   "http://challonge.com/WTC3smash"
   "http://challonge.com/NorCow3"
   "http://challonge.com/NorCow4"
   ])


(def dont-you-dair
  [
   "http://competeleaguesmash.challonge.com/dyd1singles"
   "https://smash.gg/tournament/competeleague-presents-don-t-you-dair-2"
   "https://smash.gg/tournament/don-t-you-dair-3"
   "https://smash.gg/tournament/don-t-you-dair-4"
   "https://smash.gg/tournament/don-t-you-dair-6"
   ])


(def smash-attack
  [
   "http://challonge.com/SmashAttack8"
   "http://challonge.com/SmashAttack9"
   "http://challonge.com/SA10"
   "http://challonge.com/PGGsa11"
   "http://challonge.com/SA12true"
   ])


(def whos-your-pappy
  [
   "http://8bit.challonge.com/wyp1singles"
   "http://8bit.challonge.com/wyp2singles"
   "https://smash.gg/tournament/who-s-your-pappy-3"
   "https://smash.gg/tournament/who-s-your-pappy-4"
   "https://smash.gg/tournament/who-s-your-pappy-5"
   "https://smash.gg/tournament/who-s-your-pappy-6"
   ])

(def super-smash-bros-in-the-morning
  [
   "http://challonge.com/smashbrosinthemorning1"
   "http://challonge.com/ssb4inthemorning2"
   ])

(def we-take-those
  [
   "http://challonge.com/gocwetakethose0"
   "http://challonge.com/cz8333ak"
   "http://challonge.com/ycuiu7tw"
   "http://challonge.com/Wetakethose8"
   "http://challonge.com/wetakethoseep8"
   "http://challonge.com/wetakethose9"
   "http://challonge.com/singleswtt9"
   "http://challonge.com/wttrp10"
   "http://challonge.com/wetakethose11"
   "http://challonge.com/wetakethose12"
   "http://challonge.com/wetakethose13"
   "http://challonge.com/wetakethose14"
   "http://challonge.com/wetakethose15"
   "http://challonge.com/wetakethose16"
   "http://challonge.com/wetakethose18"
   "http://challonge.com/wetakethose19"
   "http://challonge.com/wetakethose20"
   "http://challonge.com/wetakethose23"
   "http://challonge.com/wetakethose24"
   "http://challonge.com/wetakethose25"
   "http://challonge.com/wetakethose26"
   "http://challonge.com/singlesWTT27"
   ])

(def afk-gaming
  [
   "http://challonge.com/afkggsm4sh4"
   "http://challonge.com/afkggsm4sh5"
   "http://challonge.com/afkggsm4sh7"
   "http://challonge.com/afkggsm4sh8"
   "http://challonge.com/afkggsm4sh9"
   "http://challonge.com/afkggsm4sh10"
   "http://challonge.com/afkggsm4sh12"
   "http://challonge.com/afkggsm4sh13"
   ])

(def smashed-potatoes
  [
   "http://challonge.com/smashedpotatoes1"
   "http://challonge.com/smashedpotatoes2"
   "http://challonge.com/SmashedPotatoes3"
   "http://challonge.com/smashedpotatoes4"
   "http://challonge.com/smashedpotatoes5"
   "http://challonge.com/smashedpotatoes6"
   "http://challonge.com/smashedpotatoes7"
   "http://challonge.com/smashedpotatoes8"
   "http://challonge.com/smashedpotatoes9"
   "http://challonge.com/smashedpotatoes10"
   "http://challonge.com/smashedpotatoes11"
   "http://challonge.com/smashedpotatoes12"
   ])

(def miscellaneous-tournaments
  [
   "http://challonge.com/UproarWiiU"
   "https://smash.gg/tournament/let-it-shine-iii"
   "http://challonge.com/gamergalasmash4singles"
   "http://challonge.com/ss1s"
   "http://challonge.com/smashedpotatoes8"
   "http://challonge.com/pppp1sm4sh"
   "http://srs.challonge.com/GN2"
   "http://srs.challonge.com/SAG2"
   "http://srs.challonge.com/WCCCSmash"
   "http://ncr2016.challonge.com/WiiU"
   "http://levelupvideogames.challonge.com/SMASHBROSWIIU2016"
   "http://csueastbaysmash.challonge.com/DYD2SM4SHSingles"
   "http://capitolfightdistrict.challonge.com/GSP"
   "http://challonge.com/NGHA2"
   "http://challonge.com/duelisthaven"
   "http://challonge.com/spudsmashsingles"
   "http://challonge.com/surfcityslamsinglesPRO"
   "http://challonge.com/minibosssmash4singles2"
   "http://bko.challonge.com/BMS4S"
   "http://bko.challonge.com/BM24"
   "https://smash.gg/tournament/extra-life-charity-smash-4-tournament-presented-by-showdown"
   "http://capitolfightdistrict.challonge.com/CGELS4S"
   "http://challonge.com/cogpog4s4singles"
   "http://challonge.com/cogpogs4amateur"
   "http://challonge.com/TheRegulation"
   "http://csueastbaysmash.challonge.com/dydsm4S"
   "http://challonge.com/sacsunsmash1"
   "http://challonge.com/lanb4strife"
   "http://challonge.com/SIOM7"
   "http://challonge.com/GameNightsSSB4"
   "http://challonge.com/smash_aau"
   "http://scusmash.challonge.com/s4weeniehut"
   "http://challonge.com/sass1sm4sh"
   "http://challonge.com/71jh912n"
   "http://challonge.com/gamerzlaststopsingles"
   "http://terrashockgg.challonge.com/PTS_WiiU"
   "http://srs.challonge.com/SSS1"
   "http://srs.challonge.com/SSS1T8"
   "http://challonge.com/WTC1"
   "http://surfcityslam.challonge.com/Top48"
   "http://challonge.com/SmashdownSingles"
   "http://challonge.com/z4zbz9bw"
   "http://challonge.com/hgm7r27b"
   "http://challonge.com/nabstershouse2smash4singles"
   "http://challonge.com/shmashcpctournament"
   "http://challonge.com/smashtarotaro1"
   "http://challonge.com/ttnw"
   "http://challonge.com/zbghh6du"
   "http://831ssb.challonge.com/outcast3"
   "http://challonge.com/gs3ssb4"
   {:url "http://brackets.godlikecombo.com/#!/sjsm4sh1"
    :date (t/date-time 2016 9 5)
    :title "Sharkade Circuit: Smash 4 - Fall 2016"}
   "http://challonge.com/mm2smash4singless"
   ])

(def to-be-organized
  [
   "http://challonge.com/sccsm4sh1"
   "http://challonge.com/singleswtt22"
   "http://challonge.com/smashatemeraldglen2"
   "http://challonge.com/spln3"
   "http://challonge.com/ssb4cure"
   "http://challonge.com/wtt17"
   "http://challonge.com/wtt21"
   "http://gzugg.challonge.com/gamerzrebooted10s"
   "http://r3.challonge.com/sb2wiiusingles"
   "http://sab.challonge.com/bbb4sm4shsingles"
   "http://sab.challonge.com/bbb5sm4shsingles"
   "http://sab.challonge.com/finalsdestination5sm4shsingles"
   "http://srs.challonge.com/bmhh2"
   "http://sslsmash.challonge.com/dtjhsjsu24Smash4"
   ])

(defn normalize-url [url]
  (let [url (string/lower-case url)]
    (cond (smashgg/matching-url? url) (smashgg/normalize-url url)
          (challonge/matching-url? url) (challonge/normalize-url url)
          :else url)))

(def tournament-urls
  (->>
   (concat the-foundry
           smash-of-the-titans
           made
           big-mamas-little-cup
           big-mamas-little-house
           big-mamas-off-season
           wombo-wednesdays
           gamerz-smash-labs
           we-take-those
           gamerz-underground
           gamerz-rebooted
           ghost-at-dc
           ncr2015
           blu42-weekly
           blu42-smash-mass
           versus
           smash-odyssey
           the-2-stock-and-the-handshake
           teke-tourney
           game-addiction
           pheonix-uprising
           pheonix-awakening
           super-south-bay-sundays
           simply-smashing
           beta-blast
           the-forge
           rise-at-ssf
           casa-del-fuego
           smash-at-kin
           revival-of-kin
           e1337ent-smash-open
           smash-at-gg
           smash-at-berkeley
           eso-tuesdays
           smash-4-cash
           did-that-just-happen
           smash-boba-and-tea
           tea-rex
           stiikx
           fair-fights
           no-fair
           smash-at-ronin
           super-slug-fighters
           fight-nights
           battleground-wii-u
           smash-for-tats
           welcome-to-cowtown
           dont-you-dair
           smash-attack
           whos-your-pappy
           super-smash-bros-in-the-morning
           bair-necessities
           share-stock-saturdays
           afk-gaming
           back-2-basics
           to-be-organized
           miscellaneous-tournaments)
   (map normalize-url)
   distinct))

(def test-urls
  [
   "http://showdowngg.challonge.com/comeonandban60"
   "https://smash.gg/tournament/smash-of-the-titans-3"
   {:url "http://brackets.godlikecombo.com/#!/sm4sh37"
    :date (t/date-time 2016 6 20)
    :title "Versus Revival ep 37"}
   ])

(def team-names
  [
   "t17"
   "ta"
   "aps"
   "ph"
   "bg"
   "sky raiders"
   "made"
   "1up"
   "nme"
   "bask"
   "pho"
   "bko"
   "8bit"
   "8 bit"
   "8-bit"
   "swarm"
   "wtf"
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
   "sin"
   "tome"
   "st"
   "dire"
   "tcm"
   "#lh"
   "qt"
   "thcm"
   "tsg"
   ])

(def aliases
  [["Crow" "Chaos Crow" "Maleficent"]
   ["Lui$" "Luis"]
   ["N4SIR" "Stryker"]
   ["Future" "Terry Bogard"]
   ["Ursa Major" "Big Mama"]
   ["Glith" "glith10" "gltih"]
   ["Domo" "Dave"]
   ["snackthyme" "snacktyme"]
   ["Kham" "k-ham"]
   ["Lolo" "Four"]
   ["Rayn" "Keios"]
   ["DJRelly" "SuperFlyingTigerMan"]
   ["KossisMOSS" "Kossimoss" "AAA Batteries?!" "Koss" "Miranda KOSSgrove"]
   ["BlueBomber22" "Blue Bomber"]
   ["DarkSilence" "Darlsilence"]
   ["DeathPheonix13" "Deathphoenix13"]
   ["Dong Dong Never Die" "Dong Dong Never Dies"]
   ["Dr. Grin" "Dr. Grim"]
   ["Toon" "418"]
   ["Agape" "SP Agape"]
   ["Tucan" "Tucan'"]
   ["Yes2DaKing" "Yes_To_The_King"]
   ["mMmK4rma" "mMm-k4rma" "K4rma"]
   ["Saucy McYolo" "SauceyMcYolo"]
   ["StarWarriorChris" "StarWariorChris"]
   ["~fade" "fade~"]
   ["DunkinLikeJordan" "Dunken like Jordan" "Dunking Like Jordan"]
   ["EUEE" "EUEE_D2"]
   ["Electric Soldier" "Electric Solider"]
   ["Gamersaurus" "Gamersaurs"]
   ["WimpLow" "Wimplo"]
   ["Kid Icarus" "Oblivion"]
   ["Goomba" "Gooomba"]
   ["HollyPop" "HolliPop"]
   ["Jackaria" "Jackaraia"]
   ["JimmyNugz" "JimmyNug"]
   ["Onii~Chan" "Onii Chan"]
   ["Patty Ricker" "Patty Ricker."]
   ["JollyOllyman" "JollyOlyman"]
   ["JustACoolDude" "JustCoolDude"]
   ["Metakirby" "Meta-kirby"]
   ["Parasite" "Not Last"]
   (comment "This is due to a bug with team name processing."
            "'BKOlC4' and 'BKO|C4' often happen so we need to strip out the l")
   ["Legit" "egit"]
   ["Jose" "FONC! Jose"]
   ["3rdEyeVision" "3rd EyeVission"]
   ["Blank" "Blanky"]
   ["Myro" "Myron"]
   ["Nik64" "Nick64"]
   ["Glacer" "Glacier"]
   ["Gmo Skee" "G-Mo Skee"]
   ["ShadowsDepth" "ShadowsDeath"]
   ["Serg!" "Surg!" "Surge!"]
   ["Snorlax" "SnorlaxPlox" "Snorlax Plo"]
   ["Dyno Wright" "BBSOWN BlackDynoWright" "BlueDyno"]
   ["Jirachinik" "Jirachinick"]
   ["Trevonte" "Trevante" "SiN | Thizz Naruto! DBZ"]
   ["QT | K4rma" "QT.k4rma"]
   ["Zeppelin" "8BIT | Zepplin" "Zepland" "Zep"]
   ["Pump Magic" "Dump Magic"]
   ["Mijo" "Mijo FUEGO" "FS MijoFeugo"]
   ["BigMama" "Alkaid"]
   ["Wolflord" "Woflord"]
   ["Mr. Javi" "Mr Jav"]
   ["SKS" "watislyfe" "SKS aka Watislyfe"]
   ["NME | Nanerz" "Cynthia"]
   ["Rickshaw" "NinjaRlink" "Richshaw" "Dickshaw"]
   ["Kitka" "Ktika"]
   ["Nova!" "MF Space" "Space" "MF" "8BIT | Masta Space" "8bitMastaSpace" "Falcon X Falcon"]
   ["SirDaniel" "Sir"]
   ["Shia Lepuff" "Demi Lovato" "Shia La Puff" "ShaiLapuff" "Shila La Puff" "Demi Lavato"]
   ["Pulse" "RH | Old man Pulse" "Pulse *ganon grunt*" "Pulse *ganon grunts*"]
   ["8BIT | Blank" "Blankey69" "8BIT | Trojan"]
   ["K10" "ss3Katen" "KIO"]
   ["DMG | Shingo" "Shing0" "Shing07"]
   ["Vermillion" "STVermillion"]
   ["BaNdt" "ARaNdomVillager" "Bandito"]
   ["TOME|Go!" "basedGO64" "OS/ basedgo64" "OS/.GO64" "GO" "GO 64" "OS~TOME`GO" "Tome" "bbaseedGO64"]
   ["Robotic Painter" "Robatic Painter"]
   ["Mr. Pink" "Mr Pink" "Pink"]
   ["Arikie" "MS | Shadow" "Arike"]
   ["GShark" "Leffen Shark" "G-Shark" "isT G Shark" "ShArK"]
   ["Arda" "Ishiey" "A" "AA" "Ish" "Im The Best Wolf"]
   ["AD" "Adriel"]
   ["ChosenL" "ChosenL*" "CoG_ChosenL"]
   ["CrispyTacoz" "Tacoz" "TRNP | CrispyTacoz *"]
   ["z" "OS/z" "xy"]
   ["FONC | GPL Chye" "FONC | KOCI"]
   ["FONC | Fist o cuffs" "FistOCuffs" "FONC | Fist'O'cuffs"]
   ["Violet" "Andrew Le"]
   ["York" "T 17/York"]
   ["Scourge" "Summus" "Lt. Skerge" "Melon Lord" "Blue"]
   ["Mr. Peabody" "peabody"]
   ["Kronos2560" "Kronos"]
   ["Krustol" "TCM/Krustol"]
   ["110"]
   ["Hitaku" "Hitaku Back Sunday"]
   ["Twich" "Twitch"]
   ["Electric Soldier" "Electric Solder"]
   ["Ninja-The-Link-Sage" "FINAL-NINJA-THE-LINK-SAGE"]
   ["Saint" " Deity"]
   ["Boba Tapioca" "Boba"]
   ["UC | DSS" "@UC DSS" "Rocky" "DSS uP" "Fuk U Sakurai"]
   ["Beast" "Daimyes" "Jimber Jangers"]
   ["MisterQ" "MrQ" "Mr. Q"]
   ["Crisis" "Tep"]
   ["Soronie" "Seronie"]
   ["C4" "PHO -C4"]
   ["Haystack" "Haystax" "Dadstax" "Haystacks" "QTHaystack"]
   ["Chaos Pro" "Sm4sh Mango aka Chaos Pro" "Smash 4 Mango aka ChaosPro" "Rocky Balboa" "Sm4sh Mango"]
   ["BKO | Choknater" "BKO | Chokenator"]
   ["Kitka" "Yung Lord Kitka" "Lord Kitka"]
   ["Pheno" "GPheno"]
   ["TheComposer" "theComposr"]
   ["Rice" "Rice-kun" "Dark Rice" "Mr.Rice" "Rice Whine"]
   ["Andy Sauro" "Andy The Albatross"]
   ["Baron Xieon" "Baron"]
   ["Shin" "Ph~ck Shin"]
   ["Warchief" "Warhief"]
   ["Tsarkhasm" "Tsharkasm"]
   ["Pink Ranger" "PLSryrslrz"]
   ["Heartsdealer" "T Heartsdealer"]
   ["GPik" "G PIC"]
   ["Thee.O.P" "TheeOP" "thee.O.P." "OS-theeOP" "OS / TheeOP" "TheOP" "OS/ theeOP"]
   ["Focast" "Foucast"]
   ["Chinito" "Chihito" "Chino"]
   ["Jeepysol" "Jeepy" "JeepySol [Degenerate without a wallet]"]
   ["My Jeans" "60 Scarabs"]
   ["Professor Dill" "Proffesor Dill" "Prof. Dill"]
   ["Happa" "srsHappa"]
   ["MaddJu5t1n" "Madd Ju5tin"]
   ["Froggie" "Froggy" "Froge"]
   ["PewPewU" "CLG.PewPewU"]
   ["Mr. Krabs" "Mr Krabs"]
   ["A Stray Cat" "Stray" "StrayCat" "A Stay Cat"]
   ["Trex Destiny" "T-Rex Destiny" "Jae Pea" "T-Rex"]
   ["AwesomeTheSauce" "AwesomeSauce"]
   ["Sinister" "NitN"]
   ["Sean" "Sean Strife"]
   ["Zoom" "Three" "Three!"]
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

(def fall-power-rankings
  ["Shaky"
   "Trevonte"
   "Zex"
   "Boringman"
   "Jeepysol"
   "Rice"
   "Virus"
   "Teb"
   "DSS"
   "Legit"
   "3xA"
   "Soulimar"
   "Nitro"
   "Stark"
   "X"
   "Jodi Bleek"
   "Scourge"
   "Sean"
   "Arikie"
   "Mocha"])

(def norcal-crews
  {"wtf" ["Shaky"
          "Trevonte"
          "DSS"
          "Rice"
          "Sean"
          "BatShark"
          "Falln"
          "Villain"]
   "tsm" ["Soulimar"
          "Boringman"
          "Legit"
          "Teb"
          "Virus"
          "Pulse"
          "JTF"
          "My Jeans"]
   "fonc" ["Stark"
           "PXslayer"
           "Chye"
           "Prodigy"
           "Gpik"
           "Arikie"
           "FYT Demi Lovato"
           "Moondowner"
           "Nik"
           "Prof. Dill"
           "Murky"
           "FistOcuffs"
           "Electric Soldier"
           "ZeonStar"
           "Xhead"
           "T3k"
           "Ninja-The-Link-Sage"
           "Jose"]
   "BasK" ["3xA"
           "Parasite"
           "Crisis"
           "MooG"
           "Ant"
           "Risai"
           "Spark"
           "Tencoth"
           "Persea"]
   "DiRe" ["JeepySol"
           "Hitaku"
           "Ctrl"
           "Beast"
           "T-Rex Destiny"
           "Count"
           "K10"
           "TuneR"]
   "1UP" ["X"
          "Phancy"
          "Nitro"
          "Solitary"
          "Aero"
          "Amigo"]
   "8BIT" ["Saint"
           "Masta Space"
           "Cindyquil"
           "Pheno"
           "Zex"
           "Mijo Fuego"
           "OhHeyDJ"
           "Chinito"
           "Three"
           "Four"
           "Blank"
           "Zeppelin"
           "M3"
           "Glacer"
           "Keios"
           "misterQ"]
   "T17" ["York"
          "NitN"
          "Big Sean"
          "A Stray Cat"
          "Superoven"
          "Bobeta"
          "Popeye"]
   "DMG" ["Kronos2560"
          "Filthy"
          "Kato"
          "Fangfire"
          "Myro"
          "Shing0"]
   "GiGa" ["Jodi Bleek"
           "R-Senal"
           "Silicosis"
           "DamEdge"
           "BliTz"
           "Athena"
           "WimpLow"
           "RoboticPainter"
           "Ghostboy"
           "Lemon"
           "Serg!"]
   "beTa" ["^9000"
           "4Eyes"
           "Haru"
           "KiwiSlam"
           "ShadowsDepth"
           "SpaceCadet"
           "Touka"]
   "TOME" ["Tsarkhasm"
           "GO!"
           "FuTure"
           "Fayt"
           "LN41"
           "Blake"
           "Drop"
           "Bonsai"
           "NathanSandwich"]
   "TCM" ["Krustol"
          "Iron Vasquez"
          "Hearts Dealer"
          "Peabody"
          "Chevaca"
          "Vidal"
          "Zodiac"]
   "PHO" ["Mocha"
          "C4"
          "Apologyman"
          "Waael"
          "ProBeans"
          "Krampus"
          "StillAlive"
          "S2H"
          "SnorlaxPlox"
          "Violet"
          "Boba Tapioca"]
   "CK" ["Valkore"
         "Whis"
         "MoMo"
         "Illusionist"
         "AceTN"
         "G-Tex"
         "Rolo"
         "Shin"]
   "GSS" ["Echobo"
          "Fampy"
          "Bun"
          "Splargh"
          "Aus"
          "GrizzLy"
          "RaiOT"
          "Santi"]
   "SRS" ["Big Mama"
          "Happa"
          "Byte"
          "Tango"]
   "DVB" ["Mary"
          "SethTheMage"
          "Diligent109"
          "H26"
          "Oz"]
   "BoN" ["CrispyTacoz"
          "Soronie"
          "Chosen_L"
          "BaNdt"
          "DJae Mist"
          "Gshark"
          "Rickshaw"
          "Soda"
          "Groxele"]
   "PH" ["JohnnyQuest"
         "SkinnyBoiDre"
         "JetStar"
         "rhys"
         "Roo"
         "Butt"]
   "ApS" ["Wobbie"
          "Varg"
          "Pauls Inferno"
          "Moke"
          "Hiro-kun"]
   "3T" ["Klanon"
         "CryBlack"
         "Zeroxxo"
         "RPGBOY"
         "Luna"
         "Ace"]
   "YAS!" ["H4VOC"
           "snackthyme"
           "Fabulous~"
           "YUGI"
           "EAZY"
           "DARK"
           "SAUCE"
           "PATATTACK"
           "Mahooki"]
   "KOQ" ["6man"
          "Greenninja"
          "Clowntown"
          "Ch0sen1"
          "Bhox"
          "FM-Gezar"
          "biribiri"]
   "QT" ["Haystack"
         "Lift"
         "Chibi"
         "Chococrow"
         "Lynx"
         "Trager"
         "PKraiden"
         "K4rma"
         "Bubbleyumm"
         "WestJeff"
         "Winslo"
         "tic"]
   "OS" ["Z"
         "GO!"
         "theeOP"
         "Boogie"
         "Casual J"]
   "#lh" ["Wind Warrior"
          "Nik64"
          "Lardarius"
          "PKFlash"
          "e102y"
          "Styx"]
   "SP" ["Pink Ranger"
         "Blue Wolf"
         "Green Monk"
         "Stuggy"
         "Dunkin like Jordan"
         "SDV"]})

(def currently-ranked-players spring-power-ranks)
(def previously-ranked-players
  (vec (difference (set (concat january-power-ranks
                                february-power-ranks
                                march-power-ranks))
                   (set currently-ranked-players))))
