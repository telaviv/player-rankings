(ns player-rankings.logic.tournament-constants
  (:require [clojure.set :refer [difference]]
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
   "http://scusmash.challonge.com/ww21s4singles"
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
   ])

(def smash-at-kin
  [
   "http://smashkin.challonge.com/smashkin1"
   "http://challonge.com/zsv9hsmy"
   "http://challonge.com/74jzzh0c"
   "http://challonge.com/Smashkin4dontgethit"
   "http://challonge.com/SmashKin5"
   ])

(def e1337ent-smash-open
  ["http://challonge.com/ESO_W1_2016"
   "http://challonge.com/ESO_W2"
   "http://challonge.com/ESO_W3"
   ])

(def smash-at-berkeley
  ["http://challonge.com/bayo"])

(def did-that-just-happen
  ["http://sslsmash.challonge.com/dtjhsjsu05Smash4"
   "http://sslsmash.challonge.com/dtjhsjsu07Smash4"
   ])

(def miscellaneous-tournaments
  [
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
          game-addiction
          pheonix-uprising
          super-south-bay-sundays
          simply-smashing
          rise-at-ssf
          casa-del-fuego
          smash-at-kin
          e1337ent-smash-open
          smash-at-berkeley
          did-that-just-happen
          miscellaneous-tournaments))

(def test-urls
  [
   "http://showdowngg.challonge.com/comeonandban12singles"
   "https://smash.gg/tournament/extra-life-charity-smash-4-tournament-presented-by-showdown"
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
   ])

(def aliases
  [["Crow" "Chaos Crow" "Maleficent"]
   ["Glith" "glith10" "gltih"]
   ["KossisMOSS" "Kossimoss" "AAA Batteries?!"]
   ["BlueBomber22" "Blue Bomber"]
   ["DarkSilence" "Darlsilence"]
   ["DeathPheonix13" "Deathphoenix13"]
   ["Dong Dong Never Die" "Dong Dong Never Dies"]
   ["Dr. Grin" "Dr. Grim"]
   ["Toon" "418"]
   ["Tucan" "Tucan'"]
   ["Yes2DaKing" "Yes_To_The_King"]
   ["mMmK4rma" "mMm-k4rma" "K4rma"]
   ["Saucy McYolo" "SauceyMcYolo"]
   ["StarWarriorChris" "StarWariorChris"]
   ["DunkinLikeJordan" "Dunken like Jordan" "Dunking Like Jordan"]
   ["EUEE" "EUEE_D2"]
   ["Electric Soldier" "Electric Solider"]
   ["Gamersaurus" "Gamersaurs"]
   ["WimpLow" "Wimplo"]
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
   ["Three" "Three!"]
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
   ["MF Space" "Space" "MF" "8BIT | Masta Space" "8bitMastaSpace"]
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
   ["Scourge" "Summus" "Lt. Skerge" "Melon Lord"]
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
   ["UC | DSS" "@UC DSS" "Rocky" "DSS uP"]
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
   ["Rice" "Rice-kun" "Dark Rice" "Mr.Rice"]
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
