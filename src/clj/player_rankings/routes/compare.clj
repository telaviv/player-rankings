(ns player-rankings.routes.compare
  (:require [player-rankings.layout :as layout]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [player-rankings.actions :refer [compare-players]]))

(defn compare-players-response [req]
  (let [{:keys [player1 player2]} (:params req)]
    {:status 200 :body (compare-players player1 player2)}))

(defroutes compare-routes
  (GET "/compare" req (compare-players-response req)))
