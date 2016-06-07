(ns player-rankings.routes.player
  (:require [compojure.core :refer [defroutes GET]]
            [player-rankings.database.players.read :as players]))

(defn player-history [req]
  (let [name (get-in req [:params :name])]
    (if (players/player-exists? name)
      {:status 200 :body (players/player-history name)}
      {:status 400 :body {:errors {:name  {:msg (str "We found no record of " name)}}}})))

(defroutes player-routes
  (GET "/player" req (player-history req)))
