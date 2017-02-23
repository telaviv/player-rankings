(ns player-rankings.routes.compare
  (:require [player-rankings.routes.schema :refer [ComparisonPage]]
            [player-rankings.layout :as layout]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [schema.core :as s]
            [player-rankings.database.players.read :as players]))

(defn- missing-players-error [player1 player2]
  (let [missing-players (players/find-missing-players [player1 player2])]
    (reduce (fn [err player]
              (let [key (condp = player
                          player1 :player1
                          player2 :player2)]
                (assoc err key {:msg (str "We found no record of " player)})))
            {} missing-players)))

(defn- same-player-error [player1 player2]
  (if (players/same-player? player1 player2)
    {:_error {:msg (str player1 " and " player2 " are the same player.")}}
    {}))

(defn- validate-players [player1 player2]
  (let [error1 (missing-players-error player1 player2)]
    (if (empty? error1)
      (same-player-error player1 player2)
      error1)))

(defn compare-players-response [req]
  (let [{:keys [player1 player2]} (:params req)
        errors (validate-players player1 player2)]
    (if (empty? errors)
      {:status 200 :body (s/validate ComparisonPage (players/compare-players player1 player2))}
      {:status 400 :body {:errors errors}})))

(defroutes compare-routes
  (GET "/compare" req (compare-players-response req)))
