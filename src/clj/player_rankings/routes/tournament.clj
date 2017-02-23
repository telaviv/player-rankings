(ns player-rankings.routes.tournament
  (:require [compojure.core :refer [defroutes GET]]
            [schema.core :as s]
            [player-rankings.routes.schema :refer [TournamentPage]]
            [player-rankings.database.tournaments.read :as tournament]))

(defn tournament-info [req]
  (let [id (get-in req [:params :id])]
    (if (tournament/tournament-exists? id)
      {:status 200 :body (s/validate TournamentPage (tournament/tournament-data (get-in req [:params :id])))}
      {:status 400 :body {:errors {:name  {:msg (str "We found no record of " id)}}}})))

(defroutes tournament-routes
  (GET "/tournament" req (tournament-info req)))
