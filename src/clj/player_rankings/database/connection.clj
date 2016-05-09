(ns player-rankings.database.connection
  (:require [clojurewerkz.neocons.rest :as nr]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))
