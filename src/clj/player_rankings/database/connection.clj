(ns player-rankings.database.connection
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]
            [player-rankings.utilities :refer [keys->keywords]]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(cypher/tquery conn "create constraint on (a:alias) assert a.name is unique")
(cypher/tquery conn "create constraint on (t:tournament) assert t.identifier is unique")

(defn cquery
  ([query] (cquery query {}))
  ([query values] (keys->keywords (cypher/tquery conn query values))))
