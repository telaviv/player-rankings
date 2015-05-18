(ns player-rankings.logic.challonge-parser
  (:require [clojure.string :as string]
            [player-rankings.secrets :refer [challonge-api-key]])
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
