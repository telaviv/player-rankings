(ns player-rankings.utilities)

(defn keys->keywords [coll]
  (into {} (for [[k v] coll] [(keyword k) v])))
