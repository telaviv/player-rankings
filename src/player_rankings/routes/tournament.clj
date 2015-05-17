(ns player-rankings.routes.tournament
  (:require [player-rankings.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]))

(defn create-tournament-page []
  (layout/render "create-tournament.html"))


(defroutes tournament-routes
  (GET "/tournament/create" [] (create-tournament-page)))
