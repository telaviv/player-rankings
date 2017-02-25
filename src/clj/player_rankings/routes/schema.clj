(ns player-rankings.routes.schema
  (:require [taoensso.timbre :refer [spy]]
            [schema.core :as s]))

(def Player
  {:aliases [s/Str]
   :name s/Str
   :rating s/Num
   :stddev s/Num
   :volatility s/Num})

(def Tournament
  {:id s/Str
   :title s/Str
   :url s/Str
   (s/optional-key :time) s/Int})

(def Match
  {:id s/Int
   :winner s/Str
   :loser s/Str
   :score s/Str
   :time s/Int
   (s/optional-key :tournament) Tournament})

(def ComparisonPage
  {:matches [Match]
   :player1 Player
   :player2 Player
   :win-percentage s/Num})

(def TournamentPage
  {:matches [Match]
   :tournament Tournament})
