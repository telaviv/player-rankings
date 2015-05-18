(ns player-rankings.logic.challonge-parser
  (:require [clojure.string :as string]
            [player-rankings.secrets :refer [challonge-api-key]]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(defn- create-base-api-url [url]
  (let [parseable-url (URL. url)
        subdomain (-> parseable-url .getHost (string/split #"\.") (get 0))
        tournament-name (-> parseable-url .getPath (string/replace "/" ""))]
    (str "http://api.challonge.com/v1/tournaments/" subdomain "-" tournament-name "/")))

(defn- create-url-by-type [url type]
  (let [base-url (create-base-api-url url)]
    (str base-url type ".json?api_key=" challonge-api-key)))

(defn- participants-url [url]
  (create-url-by-type url "participants"))

(defn- matches-url [url]
  (create-url-by-type url "matches"))

(defn- make-request [api-url]
  (-> api-url client/get :body json/read-str))

(defn- participant-name-by-id [id participants]
  (let [participant (first (filter #(= id (get-in % ["participant" "id"])) participants))]
    (get-in participant ["participant" "display_name"])))

(defn- get-winner [match]
  (let [core-match (match "match")]
    (if (= (core-match "winner_id" ) (core-match "player1_id")) 1 2)))

(defn- merge-matches-and-participants [matches participants]
  (letfn [(participant-finder [key]
            (fn [match]
              (participant-name-by-id (get-in match ["match" key]) participants)))
          (merge-match [match]
            {:player-one ((participant-finder "player1_id") match)
             :player-two ((participant-finder "player2_id") match)
             :scores (get-in match ["match" "scores_csv"])
             :winner (get-winner match)})]
    (map merge-match matches)))

(defn get-matches-from-url [url]
  (let [matches (-> url matches-url make-request)
        participants (-> url participants-url make-request)]
    (merge-matches-and-participants matches participants)))
