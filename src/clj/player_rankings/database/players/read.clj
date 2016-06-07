(ns player-rankings.database.players.read
  (:require [clojure.set :refer [difference]]
            [clojure.string :as string]
            [clojurewerkz.neocons.rest.cypher :as cypher]
            [taoensso.timbre :refer [spy]]
            [taoensso.timbre.profiling :refer [defnp]]
            [player-rankings.database.connection :refer [conn cquery]]
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

(defnp find-missing-players [players]
  (let [query (str "unwind {players} as player "
                   "match (a:alias {name: player}) "
                   "return collect(a.name) as names")
        normalized (map normalize-name players)
        nmap (zipmap normalized players)
        results (-> (cquery query {:players normalized}) first :names)
        missing (difference (set normalized) (set results))]
    (map #(get nmap %) missing)))

(defnp player-exists? [player]
  (= 0 (count (find-missing-players [player]))))

(defnp same-player? [p1 p2]
  (let [query (str "match (al:alias)-[:aliased_to]-(p:player)-[:aliased_to]-(bl:alias) "
                   "where al.name in {players} and bl.name in {players} "
                   "return count(*) as same ")
        players (map normalize-name [p1 p2])
        result (-> (cquery query {:players players}) first :same)]
    (not (= result 0))))

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

(defn- get-existing-player-data [names]
  (comment "not guaranteed to be sorted")
  (let [query (str "match (p:player)-[:aliased_to]-(al:alias) "
                   "where al.name in {names} "
                   "return al.name as normalized, "
                   "p.provisional_rating[0] as rating,"
                   "p.provisional_rating[1] as stddev")
        normalized (map normalize-name names)
        alias-map (zipmap normalized names)
        data (cquery query {:names normalized})]
    (map (fn [player]
           {:name (get alias-map (:normalized player))
            :rating (:rating player)
            :stddev (:stddev player)
            :new false})
         data)))

(defn- new-player [name]
  {:rating (:rating rankings/default-rating)
   :stddev (:rd rankings/default-rating)
   :name name
   :new true})

(defn- load-player-data-by-name [names]
  (let [missing-players (find-missing-players names)
        existing-players (difference (set names) (set missing-players))]
    (concat (get-existing-player-data existing-players)
            (map new-player missing-players))))

(defn- transform-scores-to-cse [players]
  (map (fn [{:keys [rating stddev] :as player}]
         (let [min (- rating (* stddev 3))
               max (+ rating (* stddev 3))]
           (dissoc (assoc player :min min :max max) :rating :stddev)))
       players))

(defnp sort-players-by-cse [names]
  (->> names
       (load-player-data-by-name)
       (transform-scores-to-cse)
       (sort-by :min)
       (reverse)))

(defn- normalize-score [score]
  (let [score-parts (rankings/score-into-parts score)]
    (string/join "-" [(apply max score-parts) (apply min score-parts)])))

(defn- normalize-compared-match [match player1 player2]
  {:tournament (:tournament match)
   :time (:time match)
   :id (:id match)
   :score (normalize-score (:score match))
   :winner (if (:won match) (:name player1) (:name player2))
   :loser (if (:won match) (:name player2) (:name player1))})

(defn- normalize-matches [matches player1 player2]
  (map #(normalize-compared-match % player1 player2) matches))

(defnp compare-players [player1 player2]
  (let [query (str "match (a:player)-[:aliased_to]-(:alias {name: {player1}}), "
                   "(b:player)-[:aliased_to]-(:alias {name: {player2}}) "
                   "with a, b "
                   "optional match (a)-[pl:played]-(m:match)-[:played]-(b), (m)-[:hosted]-(t:tournament) "
                   "with {tournament: t.title, won: pl.won, score: m.score, "
                   "time: m.time, id: id(m)} as match, a, b "
                   "order by m.time desc "
                   "with collect(distinct match) as matches, a, b "
                   "return "
                   "case when matches[0]['id'] is null then [] "
                   "else matches end as matches, "
                   "{name: a.name, rating: a.provisional_rating[0], stddev: a.provisional_rating[1], "
                   "aliases: a.aliases, volatility: a.provisional_rating[2]} as player1, "
                   "{name: b.name, rating: b.provisional_rating[0], stddev: b.provisional_rating[1], "
                   "aliases: b.aliases, volatility: b.provisional_rating[2]} as player2 ")
        p1name (normalize-name player1)
        p2name (normalize-name player2)
        results (cquery query {:player1 p1name :player2 p2name})
        {:keys [player1 player2 matches]} (-> results first keys->keywords)
        nmatches (normalize-matches matches player1 player2)
        win-percentage (rankings/win-percentage player1 player2)]
    {:player1 player1 :player2 player2 :matches nmatches :win-percentage win-percentage}))

(defnp player-history [player]
  (let [query (str "match (al:alias {name: {name}})-[:aliased_to]-(p:player)-[pl:played]-(m:match), "
                   "(m)--(t:tournament), (m)-[plo:played]-(o:player) "
                   "with p, {tournament: t.title, won: pl.won, score: m.score, time: m.time, "
                   "id: id(m), opponent: o.name} as match "
                   "order by match.time desc "
                   "return {name: p.name, rating: p.provisional_rating[0], stddev: p.provisional_rating[1], "
                   "aliases: p.aliases, volatility: p.provisional_rating[2]} as player, "
                   "collect(match) as matches ")
        normalized (normalize-name player)]
    (first (cquery query {:name normalized}))))

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
