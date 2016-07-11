(ns player-rankings.routes.tournament
  (:require [compojure.core :refer [defroutes GET]]
            [player-rankings.database.tournaments.read :refer [tournament-data]]))

(defn tournament-info [req]
  {:status 200 :body (tournament-data (get-in req [:params :id]))})

(defroutes tournament-routes
  (GET "/tournament" req (tournament-info req)))
