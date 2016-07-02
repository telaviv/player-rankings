(ns player-rankings.parsers.tournament-urls
  (:require [player-rankings.parsers.smashgg :as smashgg]
            [player-rankings.parsers.challonge :as challonge]
            [player-rankings.parsers.godlike-combo :as godlike]
            [taoensso.timbre :refer [spy]]
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

(def data-parsers
  [{:matching-url? challonge/matching-url?
    :get-tournament-data challonge/get-tournament-data}
   {:matching-url? smashgg/matching-url?
    :get-tournament-data smashgg/get-tournament-data}
   {:matching-url? godlike/matching-url?
    :get-tournament-data godlike/get-tournament-data}])

(defn- tournament-data-from-parser [parser url]
  (s/validate TournamentData ((:get-tournament-data parser) url)))

(defn url-from-meta-url [meta-url]
  (if (string? meta-url)
    meta-url
    (:url meta-url)))

(defn get-tournament-data [meta-url]
  (let [url (url-from-meta-url meta-url)]
    (reduce (fn [tournament-data parser]
              (if ((:matching-url? parser) url)
                (tournament-data-from-parser parser meta-url)
                tournament-data))
            nil data-parsers)))
