(ns player-rankings.logic.database
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nodes]
            [clojurewerkz.neocons.rest.labels :as labels]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [clojurewerkz.neocons.rest.relationships :as relationships]
            [player-rankings.secrets :refer [neo4j-username neo4j-password]]))

(def conn (nr/connect
           (str "http://" neo4j-username ":" neo4j-password "@localhost:7474/db/data/")))

(defn- create-tournament-node [tournament]
  (let [node (nodes/create conn tournament)]
    (labels/add conn node "tournament")
    node))

(defn- create-match-node [match]
  (let [node (nodes/create conn {:score (:scores match) :time (:time match)})]
    (labels/add conn node "match")
    node))

(defn- create-player-node [player-name]
  (let [node (nodes/create conn {:name player-name})]
    (labels/add conn node "player")
    node))

(defn create-player-nodes [matches]
  (let [first-players (map :player-one matches)
        second-players (map :player-two matches)
        unique-players (distinct (concat first-players second-players))
        player-nodes (map create-player-node unique-players)]
    (zipmap unique-players player-nodes)))

(defn- create-match-player-relationship [match player player-number is-winner]
  (relationships/create conn player match :played {:player player-number, :won is-winner}))

(defn- create-match-graph [match player-nodes]
  (let [match-node (create-match-node match)
        player1-node (player-nodes (:player-one match))
        player2-node (player-nodes (:player-two match))]
    (create-match-player-relationship match-node player1-node 1 (= 1 (:winner match)))
    (create-match-player-relationship match-node player2-node 2 (= 2 (:winner match)))
        match-node))

(defn- create-match-graphs [matches]
  (let [player-nodes (create-player-nodes matches)]
    (map #(create-match-graph % player-nodes) matches)))

(defn create-tournament-graph [tournament-data]
  (let [tournament-node (create-tournament-node (:tournament tournament-data))
        match-nodes (create-match-graphs (:matches tournament-data))]
    (doseq [match-node match-nodes]
      (relationships/create conn tournament-node match-node :hosted))
    tournament-node))

(defn raw-match-information []
  (let [query (str "match (player:player)-[played:played]-(game:match) "
                   "return id(player) as player_id, id(played) as played_id, played.won as won "
                   "order by game.time")]
    (vec (cypher/tquery conn query))))

(defn match-information-by-player []
  (reduce (fn [coll match]
            (let [player-id (match "player_id")
                  match-record {:played-id (match "played_id") :won (match "won")}]
              (if (contains? coll player-id)
                (assoc coll player-id (conj (coll player-id) match-record))
                (assoc coll player-id [match-record]))))
          {} (raw-match-information)))
