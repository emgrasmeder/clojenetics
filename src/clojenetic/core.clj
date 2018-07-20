(ns clojenetic.core)

(defn set-terminals [state terminals]
  (println "setting terminals: " terminals)
  (assoc state :terminals terminals))

(defn set-numbers [state numbers]
  (println "setting numbers: " numbers)
  (assoc state :numbers numbers))

(defn set-functions [state functions]
  (println "setting functions: " functions)
  (assoc state :functions functions))

(defn set-objective-fn [state fn]
  (println "setting objective-fn: " fn)
  (assoc state :objective-fn fn))

(defn set-generation-limit [state limit]
  (println "setting generation-limit: " limit)
  (assoc state :generation-limit limit))

(defn rand-terminal
  [terminals numbers]
  (rand-nth (concat terminals numbers)))

(defn create-tree
  [{:keys [generation-limit terminals numbers functions :as state]}]
  (prn state)
  (if (or (zero? generation-limit)
          (< (rand) 0.5) (rand-terminal terminals numbers))
    (let [[func arity] (rand-nth functions)
          state (assoc state :generation-limit (dec generation-limit))]
      (cons func (repeatedly arity #(create-tree state))))))

(defn solve-objective-fn [state min-or-max]
  (println "solving objective function with " min-or-max)
  (let [proposed-solution (create-tree state)]
    (prn proposed-solution)))