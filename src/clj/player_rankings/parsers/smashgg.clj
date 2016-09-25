(ns player-rankings.parsers.smashgg
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(defn- tournament-slug [url]
  (second (re-find #"https://smash.gg/tournament/(.*)" url)))

(defn- general-info-api-url [url]
  (let [slug (tournament-slug url)]
    (str "https://smash.gg/api/-/resource/gg_api./tournament/"
         slug
         ";expand=%5B%22groups%22%2C%22tournament%22%2C%22event%22%5D"
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

(defn singles-event-id [tournament-info]
  (->> (get-in tournament-info [:entities :event])
       (filter #(= "Wii U Singles" (:name %)))
       (first)
       (:id)))

(defn get-tournament-information [url]
  (let [tournament-info (-> url general-info-api-url make-request)
        group-ids (map :id (get-in raw-tournament-information [:entities :groups]))
        event-id (singles-event-id tournament-info)]
    {:event-id event-id
     :brackets (brackets-from-ids group-ids)
     :tournament (get-in raw-tournament-information [:entities :tournament])}))

(defn- get-participants [brackets]
  (let [participants (mapcat #(get-in % [:entities :player]) brackets)]
    (reduce (fn [acc participant]
              (assoc acc
                     (Integer/parseInt (:entrantId participant))
                     (participant-name participant)))
            {} participants)))

(defn- filter-matches [matches event-id]
  (filter (fn [match]
            (and (= (:eventId match) event-id)
                 (not (or (nil? (:entrant1Id match))
                          (nil? (:entrant2Id match))
                          (nil? (:winnerId match))))))
          matches))

(defn- get-matches [brackets]
  (filter-matches (mapcat #(get-in % [:entities :sets]) brackets)) event-id)

(defn- score-from-match [match]
  (letfn [(get-score [key]
            (or (key match) -1))]
    (str (get-score :entrant1Score) "-" (get-score :entrant2Score))))

(defn- winner-from-match [match]
  (cond (= (:entrant1Id match) (:winnerId match)) 1
        (= (:entrant2Id match) (:winnerId match)) 2))

(defn- times-from-tournament [tournament]
  (let [start-time (* 1000 (:startAt tournament))]
    (if (:endAt tournament)
      {:start-time start-time :updated-time (* 1000 (:endAt tournament))}
      {:start-time start-time :updated-time start-time})))

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
          :time (* 1000 (:completedAt match))
          :winner (winner-from-match match)})
       matches))

(defn- identifier [tournament]
  (str "smashgg-" (get-in tournament [:slugs 0])))

(defn- normalized-tournament [tournament url]
  (let [{:keys [start-time updated-time]} (times-from-tournament tournament)]
    {:identifier (identifier tournament)
     :title (:name tournament)
     :started_at start-time
     :updated_at updated-time
     :url url}))

(defn get-tournament-data [url]
  (let [{:keys [brackets tournament event-id]} (get-tournament-information url)
        participants (get-participants brackets)
        matches (get-matches brackets event-id)]
    {:participants (normalize-participants participants)
     :matches (merge-matches-and-participants matches participants)
     :tournament (normalized-tournament tournament url)}))

(defn matching-url? [url]
  (assert (-> url nil? not))
  (re-matches #".*smash.gg.*" url))
