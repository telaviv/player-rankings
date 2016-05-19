(ns player-rankings.database.players.read
  (:require [clojure.string :as string]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.database.connection :refer [conn]]
            [player-rankings.logic.rankings :as rankings]
            [player-rankings.logic.tournament-constants :as constants]
            [player-rankings.utilities :refer [keys->keywords]]))

(defn- remove-common-team-names [lowercased-player-name team-names]
  (let [space-team-names (map #(str % " ") team-names)
        i-team-names (map #(str % "i") space-team-names)
        l-team-names (map #(str % "l") space-team-names)
        strings-to-remove (concat i-team-names l-team-names space-team-names)]
    (reduce (fn [acc team-name]
              (if (.startsWith acc team-name)
                (string/trim (string/replace-first acc team-name "")) acc))
            lowercased-player-name strings-to-remove)))

(defn normalize-name
  ([player-name] (normalize-name player-name constants/team-names))
  ([player-name team-names]
   (-> player-name
       (string/replace #"!" " ")
       (string/replace #"_" " ")
       (string/replace #"\(.*\)" "")
       (string/split #"\|")
       last
       string/trim
       string/lower-case
       (remove-common-team-names team-names)
       (string/replace #"\s" ""))))

(defn- merge-in-alias-list [alias-map alias-list]
  (let [normalized-aliases (map normalize-name alias-list)
        matching-aliases (distinct (filter #(contains? alias-map %) normalized-aliases))
        merged-players (distinct (mapcat #(get alias-map %) matching-aliases))]
    (reduce #(assoc %1 %2 merged-players) alias-map matching-aliases)))

(defn- merge-players-by-explicit-alias [alias-map]
  (reduce (fn [coll alias-list]
            (merge-in-alias-list coll alias-list))
          alias-map constants/aliases))

(defn- get-matching-players-from-alias-map
  ([alias-map player] (get-matching-players-from-alias-map alias-map player (:aliases player)))
  ([alias-map player aliases]
   (let [normalized-aliases (map normalize-name aliases)
         alias-to-check (first normalized-aliases)]
     (cond
       (empty? aliases) [player]
       (contains? alias-map alias-to-check) (conj (alias-map alias-to-check) player)
       :else (recur alias-map player (rest aliases))))))

(defn merge-aliases [players]
  (let [aliases (mapcat :aliases players)]
    (reduce
     (fn [acc alias]
       (let [normalized-acc (map normalize-name acc)
             normalized-alias (normalize-name alias)]
         (if (some #(= normalized-alias %) normalized-acc)
           acc
           (conj acc alias)))) [] aliases)))

(defn- add-player-to-alias-map [alias-map player]
  (let [matching-players (get-matching-players-from-alias-map alias-map player)
        aliases (merge-aliases matching-players)]
    (apply assoc (concat [alias-map]
                         (interleave (map normalize-name aliases) (repeat matching-players))))))

(defn create-alias-map [players]
  (merge-players-by-explicit-alias
   (reduce #(add-player-to-alias-map %1 %2) {} players)))

(defnp get-players-for-rank-sorting []
  (let [query (str "match (p:player) "
                   "return id(p) as id, "
                   "p.aliases as aliases, "
                   "p.provisional_rating[0] as rating,"
                   "p.provisional_rating[1] as stddev")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))

(defn get-players-by-name-for-rank-sorting [player-names]
  (let [players (get-players-for-rank-sorting)
        alias-map (create-alias-map players)]
    (map (fn [player-name]
           (let [normalized-name (normalize-name player-name)]
             (if (contains? alias-map normalized-name)
               (assoc (first (get alias-map normalized-name)) :name player-name :new false)
               {:aliases [player-name]
                :rating (:rating rankings/default-rating)
                :stddev (:rd rankings/default-rating)
                :name player-name
                :new true})))
         player-names)))

(defn- normalize-score [score]
  (let [score-parts (rankings/score-into-parts score)]
    (string/join "-" [(apply max score-parts) (apply min score-parts)])))

(defn- normalize-compared-match [match player1 player2]
  {:tournament (:tournament match)
   :time (:time match)
   :score (normalize-score (:score match))
   :winner (if (:won match) player1 player2)
   :loser (if (:won match) player2 player1)})

(defn compare-players [player1 player2]
  (let [query (str "match (a:player)-[pl:played]-(m:match), "
                   "(m)--(b:player), (m)--(t:tournament) "
                   "where id(a) = {aid} and id(b) = {bid} "
                   "return t.title as tournament, "
                   "pl.won as won, m.score as score, m.time as time "
                   "order by time desc ")
        matches (cypher/tquery conn query {:aid (:id player1) :bid (:id player2)})]
    (->> matches
         (map keys->keywords)
         (filter #(-> % :score rankings/is-disqualifying-score not))
         (map #(normalize-compared-match % (:name player1) (:name player2))))))

(defnp compare-players-fast [player1 player2]
  (let [query (str "match (a:player)-[:aliased_to]-(al:alias {name: {player1}}), "
                   "(b:player)-[:aliased_to]-(bl:alias {name: {player2}}), "
                   "(a)-[pl:played]-(m:match)-[:played]-(b), (m)-[:hosted]-(t:tournament) "
                   "with {title: t.title, won: pl.won, score: m.score, time: m.time} as match, a, b "
                   "order by m.time desc "
                   "with collect(match) as matches, a, b "
                   "return matches, "
                   "{name: a.name, rating: a.provisional_rating[0], stddev: a.provisional_rating[1], "
                   "aliases: a.aliases, volatility: a.provisional_rating[2]} as player1, "
                   "{name: b.name, rating: b.provisional_rating[0], stddev: b.provisional_rating[1], "
                   "aliases: b.aliases, volatility: b.provisional_rating[2]} as player2 ")
        p1name (normalize-name player1)
        p2name (normalize-name player2)
        results (cypher/tquery conn query {:player1 p1name :player2 p2name})
        {:keys [player1 player2 matches]} (-> results first keys->keywords)
        nmatches (map #(normalize-compared-match % player1 player2) matches)
        win-percentage (rankings/win-percentage player1 player2)]
    {:player1 player1 :player2 player2 :matches nmatches :win-percentage win-percentage}))

(defnp get-existing-players []
  (let [query (str "match (p:player) "
                   "return id(p) as id, p.aliases as aliases")
        data (cypher/tquery conn query)]
    (map keys->keywords data)))


(defn get-matching-player [player-name players]
  (some #(when (some (fn [existing-player-name]
                       (= (normalize-name player-name)
                          (normalize-name existing-player-name)))
                     (map normalize-name (:aliases %))) %)
        players))
