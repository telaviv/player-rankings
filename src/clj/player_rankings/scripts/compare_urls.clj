(ns player-rankings.scripts.compare-urls
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [player-rankings.parsers.smashgg :as smashgg]
            [player-rankings.parsers.challonge :as challonge]
            [player-rankings.logic.tournament-constants :refer [tournament-urls]]))

(def NORCAL-TOURNAMENT-URL
  "https://docs.google.com/spreadsheets/d/1fAS-RzrrtlGsb0975jA7cqBj3i8I8YwKTAy7IlQ1X_k/pubhtml#")

(def NEW-URL-BLACKLIST
  ["http://gameworksseattle.challonge.com/SWPS4Top32"
   "http://brackets.godlikecombo.com/#!/tourney-direct/-8116973707330696792/standing"
   "http://challonge.com/WWLSm4sh/groups"
   ])

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn norcal-google-redirects []
  (->> (html/select (fetch-url NORCAL-TOURNAMENT-URL) [:tr :td :a])
       (filter #(= "Singles" (html/text %)))
       (map (fn [elem] (-> elem :attrs :href)))))

(defn normalized-old-tournament-urls []
  (set (map (fn [url]
              (if (string? url)
                url
                (:url url)))
            tournament-urls)))

(defn new-tournament-urls []
  (map (fn [gurl]
         (-> gurl url :query (get "q")))
       (norcal-google-redirects)))

(defn normalized-new-tournament-urls []
  (reduce (fn [urls url]
            (let [normalized (normalize-url url)]
              (if (nil? normalized)
                urls
                (conj urls normalized))))
          #{} (new-tournament-urls)))

(defn unadded-urls []
  (difference (normalized-new-tournament-urls)
              (normalized-old-tournament-urls)))

(defn normalize-url [url]
  (cond (smashgg/matching-url? url) (smashgg/normalize-url url)
        (challonge/matching-url? url) (challonge/normalize-url url)
        :else nil))

(defn print-missing-urls []
  (doseq [url (sort (unadded-urls))] (println (format "\"%s\"" url))))
