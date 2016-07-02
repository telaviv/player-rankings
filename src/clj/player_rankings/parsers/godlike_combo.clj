(ns player-rankings.parsers.godlike-combo
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [taoensso.timbre :refer [spy info]]
            [clj-time.coerce :as c])
  (:import [java.net URL]))

(defn- tournament-slug [url]
  (second (re-matches #"http://brackets.godlikecombo.com/#!/(.*)" url)))

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
            (not (or (nil? (get-in match [:player1]))
                     (nil? (get-in match [:player2]))
                     (get-in match [:player1 :isByePlayer])
                     (get-in match [:player2 :isByePlayer]))))
          matches))

(defn- get-matches [tournament]
  (let [winners (get-in tournament [:tourney :winners])
        losers (get-in tournament [:tourney :losers])
        grand-finals (get-in tournament [:tourney :grandFinals])
        raw-matches (concat winners losers grand-finals)]
    (filter-matches (mapcat :matches raw-matches))))

(defn- strip-w-l [name]
  (let [match (re-matches #"(.*) \((W|L)\)" name)]
    (if (nil? match)
      name
      (second match))))

(defn- strip-underscores [name]
  (let [match (re-matches #"(.*?)(_+)" name)]
    (if match
      (second match)
      name)))

(defn- normalize-name [raw-name]
  (-> raw-name strip-w-l strip-underscores))

(defn- get-participants [matches]
  (reduce (fn [names match]
            (into names
                  [(normalize-name (:player1UiString match))
                   (normalize-name (:player2UiString match))]))
          #{} matches))

(defn- normalize-participants [participants]
  (map (fn [participant-name]
         (comment "we should figure out a way to get the real placement.")
         {:name participant-name
          :placement -1})
       participants))

(defn- score-from-match [match]
  (str (:player1Wins match) "-" (:player2Wins match)))

(defn- winner-from-match [match]
  (if (> (:player1Wins match) (:player2Wins match)) 1 2))

(defn- normalize-matches [matches time]
  (comment "we need to change the time to be dynamic.")
  (map (fn [match]
         {:player-one (normalize-name (:player1UiString match))
          :player-two (normalize-name (:player2UiString match))
          :scores (score-from-match match)
          :time time
          :winner (winner-from-match match)})
       matches))

(defn- identifier [tournament]
  (str "godlike-combo-" (:bracketUrlPath tournament)))

(defn- normalize-tournament [raw-tournament time title url]
  (let [tournament (:tourney raw-tournament)]
    {:identifier (identifier tournament)
     :title title
     :started_at time
     :updated_at time
     :url url}))

(defn get-tournament-data [{:keys [url date title]}]
  (let [tournament (-> url api-url make-request)
        matches (get-matches tournament)
        participants (get-participants matches)
        time (c/to-long date)]
    {:matches (normalize-matches matches time)
     :participants (normalize-participants participants)
     :tournament (normalize-tournament tournament time title url)}))

(defn matching-url? [url]
  (re-matches #".*godlikecombo.com.*" url))
