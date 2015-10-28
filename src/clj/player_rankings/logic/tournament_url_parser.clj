(ns player-rankings.logic.tournament-url-parser
  (:require [player-rankings.logic.smashgg-parser :as smashgg]
            [player-rankings.logic.challonge-parser :as challonge]
            [schema.core :as s]))

(def Participant
  {:name s/Str
   :placement s/Int})

(def Match
  {:player-one s/Str
   :player-two s/Str
   :scores s/Str
   :time s/Int
   :winner s/Int})

(def Tournament
  {:identifier s/Str
   :title s/Str
   :started_at s/Int
   :updated_at s/Int
   :url s/Str
   (s/optional-key :image_url) s/Str})

(def TournamentData
  {:participants [Participant]
   :matches [Match]
   :tournament Tournament})

(defn get-tournament-data [url]
  (cond
    (challonge/matching-url? url) (s/validate TournamentData (challonge/get-tournament-data url))
    (smashgg/matching-url? url) (s/validate TournamentData (smashgg/get-tournament-data url))))
