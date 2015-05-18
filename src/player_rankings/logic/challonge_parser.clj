(ns player-rankings.logic.challonge-parser
  (:require [clojure.string :as string])
  (:import [java.net URL]))

(defn create-base-api-url [url]
  (let [parseable-url (URL. url)
        subdomain (-> parseable-url .getHost (string/split #"\.") (get 0))
        tournament-name (-> parseable-url .getPath (string/replace "/" ""))]
    (str "http://api.challonge.com/v1/tournaments/" subdomain "-" tournament-name)))
