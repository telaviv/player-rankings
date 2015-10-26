(ns player-rankings.logic.smashgg-parser
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(def tournament-url "https://smash.gg/tournament/come-on-and-ban-33")

(defn- tournament-slug [url]
  (second (re-find #"https://smash.gg/tournament/(.*)" url)))

(defn- general-info-api-url [url]
  (let [slug (tournament-slug url)]
    (str "https://smash.gg/api/-/resource/gg_api./tournament/"
         slug
         ";expand=%5B%22groups%22%5D"
         ";slug="
         slug)))

(defn- bracket-url [group-id]
  (str "https://smash.gg/api/-/resource/gg_api./phase_group/"
       group-id
       ";admin=undefined;expand=%5B%22sets%22%2C%22entrants%22%5D;id="
       group-id
       "reset=false"))

(defn- make-request [api-url]
  (-> api-url client/get :body (json/read-str :key-fn keyword)))

(defn- group-ids-from-url [url]
  (let [bracket-info (-> url general-info-api-url make-request)]
    (map :id (get-in bracket-info [:entities :groups]))))

(defn- participant-name [participant]
  (if (empty? (:prefix participant))
    (:gamerTag participant)
    (str (:prefix participant) " | " (:gamerTag participant))))

(defn brackets-from-url [url]
  (pmap (fn [group-id] (-> group-id bracket-url make-request))
        (group-ids-from-url url)))

(defn- get-participants [brackets]
  (let [participants (mapcat #(get-in % [:entities :player]) brackets)]
    (reduce (fn [acc participant]
              (assoc acc
                     (Integer/parseInt (:entrantId participant))
                     (participant-name participant)))
            {} participants)))

(defn- filter-matches [matches]
  (filter (fn [match]
            (not (or (nil? (:entrant1Id match))
                     (nil? (:entrant2Id match))
                     (nil? (:winnerId match)))))
          matches))

(defn- get-matches [brackets]
  (filter-matches (mapcat #(get-in % [:entities :sets]) brackets)))

(defn- score-from-match [match]
  (letfn [(get-score [key]
            (or (key match) -1))]
    (str (get-score :entrant1Score) "-" (get-score :entrant2Score))))

(defn- merge-matches-and-participants [matches participants]
  (map (fn [match]
         {:player-one (participants (:entrant1Id match))
          :player-two (participants (:entrant2Id match))
          :scores (score-from-match match)
          :time (:completedAt match)
          :winner (participants (:winnerId match))})
       matches))

(defn get-tournament-data [url]
  (let [brackets (brackets-from-url url)
        participants (get-participants brackets)
        matches (get-matches brackets)]
    (merge-matches-and-participants matches participants)))
