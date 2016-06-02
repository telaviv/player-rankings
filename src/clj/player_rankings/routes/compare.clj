(ns player-rankings.routes.compare
  (:require [player-rankings.layout :as layout]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [player-rankings.database.players.read :as players]))

(defn- validate-players [player1 player2]
  (let [missing-players (players/find-missing-players [player1 player2])]
    (reduce (fn [err player]
              (let [key (condp = player
                          player1 :player1
                          player2 :player2)]
                (assoc err key {:msg (str "We found no record of " player)})))
            {} missing-players)))

(defn compare-players-response [req]
  (let [{:keys [player1 player2]} (:params req)
        errors (validate-players player1 player2)]
    (if (empty? errors)
      {:status 200 :body (players/compare-players player1 player2)}
      {:status 400 :body {:errors errors}})))

(defroutes compare-routes
  (GET "/compare" req (compare-players-response req)))
