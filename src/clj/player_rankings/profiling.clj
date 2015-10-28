(ns player-rankings.profiling)

(defmacro timed [expr]
  (let [sym (= (type expr) clojure.lang.Symbol)]
    `(let [start# (. System (nanoTime))
           return# ~expr
           res# (if ~sym
                    (resolve '~expr)
                    (resolve (first '~expr)))]
       (prn (str "Timed "
           (:name (meta res#))
           ": " (/ (double (- (. System (nanoTime)) start#)) 1000000.0) " msecs"))
       return#)))

(defn doall-recur [s]
  (cond
   (map? s) (reduce
             (fn [r i]
               (merge {(first i)
                       (doall-recur (second i))} r))
             {} s)
   (seq? s) (doall
             (map doall-recur
                  s))
   :else s))
