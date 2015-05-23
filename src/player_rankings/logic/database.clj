(ns player-rankings.logic.database
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(defn- create-tournament-node [tournament]
  (let [node (nodes/create conn tournament)]
    (labels/add conn node "tournament")
    node))

(defn- create-match-node [match]
  (let [node (nodes/create conn {:score (:scores match)})]
    (labels/add conn node "match")
    node))

(defn- create-player-node [player]
  (let [node (nodes/create conn {:name player})]
    (labels/add conn node "player")
    node))

(defn- create-match-player-relationship [match player player-number is-winner]
  (relationships/create conn player match :played {:player player-number, :won is-winner}))

(defn- create-match-graph [match]
  (let [match-node (create-match-node match)
        player1-node (create-player-node (:player-one match))
        player2-node (create-player-node (:player-two match))]
    (create-match-player-relationship match-node player1-node 1 (= 1 (:winner match)))
    (create-match-player-relationship match-node player2-node 2 (= 2 (:winner match)))
    match-node))

(defn- create-tournament-graph [tournament-data]
  (let [tournament-node (create-tournament-node (:tournament tournament-data))
        match-nodes (map create-match-graph (:matches tournament-data))]
    (doseq [match-node match-nodes]
      (relationships/create conn tournament-node match-node :hosted))
    tournament-node))
