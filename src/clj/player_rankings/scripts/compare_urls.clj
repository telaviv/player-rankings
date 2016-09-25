(ns player-rankings.scripts.compare-urls
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]
            [cemerick.url :refer [url]]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [player-rankings.logic.tournament-constants :refer [tournament-urls]]))

(def NORCAL-TOURNAMENT-URL
  "https://docs.google.com/spreadsheets/d/1fAS-RzrrtlGsb0975jA7cqBj3i8I8YwKTAy7IlQ1X_k/pubhtml#")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn norcal-google-redirects []
  (->> (html/select (fetch-url NORCAL-TOURNAMENT-URL) [:tr :td.s10])
       (filter #(= "Singles" (html/text %)))
       (map (fn [elem] (-> elem :content first :attrs :href)))))

(defn normalized-old-tournament-urls []
  (map (fn [url]
         (if (string? url)
           url
           (:url url)))
       tournament-urls))

(defn new-tournament-urls []
  (map (fn [gurl]
         (-> gurl url :query (get "q")))
       (norcal-google-redirects)))

(defn unadded-urls []
  (difference (set (new-tournament-urls))
              (set (normalized-old-tournament-urls))))

(defn print-missing-urls []
  (doseq [url (sort (unadded-urls))] (println url)))
