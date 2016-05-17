(ns player-rankings.database.players.write
  (:require [clojurewerkz.neocons.rest.cypher :as cypher]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.utilities :refer [keys->keywords]]))

(defn create-new-player-nodes [player-names]
  (let [query (str "unwind {names} as name "
                   "create (p:player {name: name, aliases: [name]}) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query {:names player-names})]
    (map keys->keywords data)))
