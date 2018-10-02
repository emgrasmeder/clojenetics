(ns clojenetics.logics.setters
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.utils :as utils]))

(defn set-terminals [state terminals]
  (log/debug "Setting terminals: " terminals)
  (assoc state :terminals terminals))

(defn set-numbers [state numbers]
  (log/debug "Setting numbers: " numbers)
  (assoc state :numbers numbers))

(defn set-functions [state functions]
  (log/debug "Setting functions: " functions)
  (assoc state :functions functions))

(defn set-objective-fn [state fn]
  (log/debug "Setting objective-fn: " fn)
  (assoc state :objective-fn fn))

(defn set-max-tree-depth [state limit]
  (log/debug "Setting max-tree-depth: " limit)
  (-> state
      (assoc :max-tree-depth limit)
      (assoc :current-tree-depth limit)))

(defn dec-current-tree-depth [state]
  (assoc state :current-tree-depth (dec (:current-tree-depth state))))

(defn set-seed-count [state seeds]
  (log/debug "Setting:seeds-remaining: " seeds)
  (assoc state :seeds-remaining seeds))

(defn set-target [state target]
  (log/debug "Setting target: " target)
  (assoc state :target target))

(defn sum-of-scores [{:keys [population] :as state}]
  (->> population
       (map :score)
       (reduce +)
       (assoc state :sum-of-scores)))

(defn set-best-tree [state]
  (log/debug "Setting best tree from" state)
  (let [f (if (= :minimize (:min-or-max-objective-fn state)) > <)
        trees (:population state)
        best-tree (if (= 1 (count trees))
                    (first trees)
                    (reduce (fn [a b]
                              (if (f (:score a) (:score b)) b a))
                            (filter #(not= :ERROR (:score %)) trees)))]
    (assoc state :best-tree best-tree)))

(defn set-new-tree [state tree]
  (log/debug "Setting new tree: " tree)
  (assoc state :population (concat (:population state) [{:tree tree}])))

(defn set-scores [{:keys [population objective-fn target] :as state}]
  "Appends a score to each tree according to a provided objective function."
  (log/debug "Setting scores for: " population)
  (let [new-population (map (fn [tree-hash]
                         (assoc tree-hash :score
                                          (utils/score-objective-fn state (:tree tree-hash)))) population)]
    (assoc state :population new-population)))

(defn dec-seeds-remaining
  [state]
  (assoc state :seeds-remaining (dec (:seeds-remaining state))))

(defn set-generations
  [state generations]
  (log/debug "Setting generations" generations)
  (assoc state :generations-remaining generations))

(defn dec-generations
  [state]
  (assoc state :generations-remaining (dec (:generations-remaining state))))

(defn set-population [state population]
  (assoc state :population population))

(defn set-min-or-max-for-obj-fn [state fn]
  (assoc state :min-or-max-objective-fn fn))