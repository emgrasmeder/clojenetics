(ns clojenetics.core
  (:require [clojure.tools.logging :as log]))

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

(defn set-tree-depth [state limit]
  (log/debug "Setting tree-depth: " limit)
  (assoc state :tree-depth limit))

(defn rand-terminal
  [terminals numbers]
  (log/debug "Choosing random terminal")
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [tree-depth terminals numbers]}]
  (if (or (zero? tree-depth) (< (rand) 0.5))
    (rand-terminal terminals numbers)
    false))

(defn create-tree
  [{:keys [tree-depth terminals numbers functions] :as state}]
  (or (try-for-terminal state)
      (let [[func arity] (rand-nth functions)
            state (set-tree-depth state (dec tree-depth))]
        (log/debugf "Recursing tree creation with state: %s" state)
        (cons func (repeatedly arity #(create-tree state))))))

(defn solve-objective-fn [state min-or-max]
  (let [tree (create-tree state)]
    (if (= 0 ((:objective-fn state) tree))
      tree
      (solve-objective-fn state min-or-max))))