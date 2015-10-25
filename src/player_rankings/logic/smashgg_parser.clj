(ns player-rankings.logic.smashgg-parser
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(def tournament-url "https://smash.gg/tournament/come-on-and-ban-33")

(defn tournament-slug [url]
  (second (re-find #"https://smash.gg/tournament/(.*)" url)))

(defn general-info-api-url [url]
  (let [slug (tournament-slug url)]
    (str "https://smash.gg/api/-/resource/gg_api./tournament/"
         slug
         ";expand=%5B%22groups%22%5D"
         ";slug="
         slug)))

(defn- make-request [api-url]
  (-> api-url client/get :body (json/read-str :key-fn keyword)))
