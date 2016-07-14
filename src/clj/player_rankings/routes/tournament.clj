(ns player-rankings.routes.tournament
  (:require [compojure.core :refer [defroutes GET]]
            [player-rankings.database.tournaments.read :as tournament]))

(defn tournament-info [req]
  (let [id (get-in req [:params :id])]
    (if (tournament/tournament-exists? id)
      {:status 200 :body (tournament/tournament-data (get-in req [:params :id]))}
      {:status 400 :body {:errors {:name  {:msg (str "We found no record of " id)}}}})))

(defroutes tournament-routes
  (GET "/tournament" req (tournament-info req)))
