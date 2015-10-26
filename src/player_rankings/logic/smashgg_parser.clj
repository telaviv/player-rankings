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
         ";expand=%5B%22groups%22%2C%22tournament%22%5D"
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

(defn- participant-name [participant]
  (if (empty? (:prefix participant))
    (:gamerTag participant)
    (str (:prefix participant) " | " (:gamerTag participant))))

(defn brackets-from-ids [group-ids]
  (pmap (fn [group-id] (-> group-id bracket-url make-request))
        group-ids))

(defn get-tournament-information [url]
  (let [raw-tournament-information (-> url general-info-api-url make-request)
        group-ids (map :id (get-in raw-tournament-information [:entities :groups]))]
    {:brackets (brackets-from-ids group-ids)
     :tournament (get-in raw-tournament-information [:entities :tournament])}))

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

(defn- normalize-participants [participants]
  (map (fn [participant-name]
         (comment "we should figure out a way to get the real placement.")
         {:name participant-name
          :placement -1})
       (vals participants)))

(defn- merge-matches-and-participants [matches participants]
  (map (fn [match]
         {:player-one (participants (:entrant1Id match))
          :player-two (participants (:entrant2Id match))
          :scores (score-from-match match)
          :time (:completedAt match)
          :winner (participants (:winnerId match))})
       matches))

(defn get-tournament-data [url]
  (let [{:keys [brackets tournament]} (get-tournament-information url)
        participants (get-participants brackets)
        matches (get-matches brackets)]
    {:participants (normalize-participants participants)
     :matches (merge-matches-and-participants matches participants)}))
