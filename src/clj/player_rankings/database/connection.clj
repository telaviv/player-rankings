(ns player-rankings.database.connection
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(cypher/tquery conn "create index on :alias(name)")
