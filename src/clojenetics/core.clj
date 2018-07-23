(ns clojenetics.core)

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

(defn set-tree-depth [state limit]
  (println "setting tree-depth: " limit)
  (assoc state :tree-depth limit))

(defn rand-terminal
  [terminals numbers]
  (println "choosing random terminal")
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [tree-depth terminals numbers]}]
  (if (or (zero? tree-depth) (< (rand) 0.5))
    (rand-terminal terminals numbers)
    false))

(defn create-tree
  [{:keys [tree-depth terminals numbers functions] :as state}]
  #_(println "Creating tree from..." state)
  (or (try-for-terminal state)
      (let [[func arity] (rand-nth functions)
            state (set-tree-depth state (dec tree-depth))]
        (println "Recursing tree creation with state: " state)
        (println "func, arity" func arity)
        (cons func (repeatedly arity #(create-tree state))))))

(defn solve-objective-fn [state min-or-max]
  (println "solving objective function with " min-or-max)
  (let [tree (create-tree state)]
    (if (= 0 ((:objective-fn state) tree))
      tree
      (solve-objective-fn state min-or-max))))