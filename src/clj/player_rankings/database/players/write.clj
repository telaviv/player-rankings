(ns player-rankings.database.players.write
  (:require [clojurewerkz.neocons.rest.cypher :as cypher]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.database.players.read :refer [normalize-name]]
            [player-rankings.utilities :refer [keys->keywords]]))

(defn create-new-player-nodes [player-names]
  (let [query (str "unwind {records} as record "
                   "create (p:player {name: record.name, aliases: [record.name]}) "
                   "create (a:alias {name: record.normalized})<-[:aliased_to]-(p) "
                   "return id(p) as id, p.aliases as aliases")
        normalized-names (map normalize-name player-names)
        records (map (fn [p n] {:name p :normalized n}) player-names normalized-names)
        data (cypher/tquery conn query {:records records})]
    (map keys->keywords data)))
