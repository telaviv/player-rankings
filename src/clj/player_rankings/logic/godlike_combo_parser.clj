(ns player-rankings.logic.godlike-combo-parser
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(def tournament-url "http://brackets.godlikecombo.com/#!/sm4shep16")

(defn- tournament-slug [url]
  (second (re-find #"http://brackets.godlikecombo.com/#!/(.*)" url)))

(defn- api-url [url]
  (let [slug (tournament-slug url)]
    (str "https://api.godlikecombo.com/godlikecombo?"
         "apicall=get_tourney_by_url_path&url_path="
         slug
         "&protocol_version=0.08&client_os=web")))

(defn- make-request [api-url]
  (-> api-url client/get :body (json/read-str :key-fn keyword)))

(defn- filter-matches [matches]
  (filter (fn [match]
            (not (or (get-in match [:player1 :isByePlayer])
                     (get-in match [:player2 :isByePlayer]))))
          matches))

(defn- get-matches [url]
  (let [raw-request (-> url api-url make-request)
        winners (get-in raw-request [:tourney :winners])
        losers (get-in raw-request [:tourney :losers])
        raw-matches (concat winners losers)]
    (filter-matches (mapcat :matches raw-matches))))

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

(defn- normalized-tournament [tournament url]
  (let [{:keys [start-time updated-time]} (times-from-tournament tournament)]
    {:identifier (get-in tournament [:slugs 0])
     :title (:name tournament)
     :started_at start-time
     :updated_at updated-time
     :url url}))

(defn get-tournament-data [url]
  (let [{:keys [brackets tournament]} (get-tournament-information url)
        participants (get-participants brackets)
        matches (get-matches brackets)]
    {:participants (normalize-participants participants)
     :matches (merge-matches-and-participants matches participants)
     :tournament (normalized-tournament tournament url)}))

(defn matching-url? [url]
  (re-matches #".*smash.gg.*" url))
