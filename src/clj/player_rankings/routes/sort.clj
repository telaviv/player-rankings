(ns player-rankings.routes.sort
  (:require [player-rankings.layout :as layout]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [player-rankings.actions :refer [sort-player-names-by-cse]]))

(defn sort-players [req]
  (let [players (json/read-str (get-in req [:params :players]))]
    {:status 200 :body (sort-player-names-by-cse players)}))

(defroutes sort-routes
  (GET "/sort" req (sort-players req)))
