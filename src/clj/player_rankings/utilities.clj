(ns player-rankings.utilities)

(defn keys->keywords [v]
  (letfn [(convert-maps [coll]
            (into {} (for [[k v] coll] [(keyword k) (keys->keywords v)])))
          (convert-sequences [seq]
            (map keys->keywords seq))]
    (cond (seq? v) (convert-sequences v)
          (map? v) (convert-maps v)
          :else v)))
