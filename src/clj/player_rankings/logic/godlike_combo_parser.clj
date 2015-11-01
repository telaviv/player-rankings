(ns player-rankings.logic.godlike-combo-parser
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:import [java.net URL]))

(def tournament-url "http://brackets.godlikecombo.com/#!/sm4shep16")

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

(defn- get-matches [url]
  (let [raw-request (-> url api-url make-request)
        winners (get-in raw-request [:tourney :winners])
        losers (get-in raw-request [:tourney :losers])
        grand-finals (get-in raw-request [:tourney :grandFinals])
        raw-matches (concat winners losers grand-finals)]
    (filter-matches (mapcat :matches raw-matches))))

(defn- strip-w-l [name]
  (let [match (re-matches #"(.*) \((W|L)\)" name)]
    (assert (= 3 (count match)) (str name " isn't in the right format"))
    (second match)))

(defn- strip-underscores [name]
  (let [match (re-matches #"(.*?)(_+)" name)]
    (if match
      (second match)
      name)))

(defn- normalize-name [raw-name]
  (-> raw-name strip-w-l strip-underscores))

(defn- get-participants [matches]
  (vec (reduce (fn [names match]
                 (into names
                       [(normalize-name (:player1UiString match))
                        (normalize-name (:player2UiString match))]))
               #{} matches)))

(defn- normalize-participants [participants]
  (map (fn [participant-name]
         (comment "we should figure out a way to get the real placement.")
         {:name participant-name
          :placement -1})
       (vals participants)))

(defn- score-from-match [match]
  (str (:player1Wins match) "-" (:player2Wins match)))

(defn- winner-from-match [match]
  (if (> (:player1Wins match) (:player2Wins match)) 1 2))

(defn- normalize-matches [matches]
  (comment "we need to change the time to be dynamic.")
  (map (fn [match]
         {:player-one (normalize-name (:player1UiString match))
          :player-two (normalize-name (:player2UiString match))
          :scores (score-from-match match)
          :time 1443729600
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
