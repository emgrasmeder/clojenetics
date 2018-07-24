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

(defn set-population-size [state population]
  (log/debug "Setting population-size: " population)
  (assoc state :population-size population))

(defn set-target [state target]
  (log/debug "Setting target: " target)
  (assoc state :target target))

(defn population-allowance [state]
  (> (:population-size state) 0))

(defn rand-terminal
  [terminals numbers]
  (log/debug "Choosing random terminal")
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [tree-depth terminals numbers population-size]}]
  (if (or (zero? tree-depth)
          (zero? population-size)
          (< (rand) 0.5))
    (rand-terminal terminals numbers)
    false))

(declare create-tree)

(defn create-subtree [{:keys [functions tree-depth] :as state}]
  (let [[func arity] (rand-nth functions)
        state (set-tree-depth state (dec (:tree-depth state)))]
    (log/debugf "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [state]
  (or (try-for-terminal state)
      (create-subtree state)))

(defn score-objective-fn [{:keys [target objective-fn] :as state} tree]
  (log/debugf "Scoring objective fn with state %s and tree %s" state tree)
  (objective-fn target tree))

(defn apply-min-or-max [{:keys [min-or-max-objective-fn]}]
  (if (= min-or-max-objective-fn :minimize)
    <
    >))

(defn get-best-tree [state]
  (log/debug "Getting best tree with state: " state)
  (let [tree (create-tree state)
        score (score-objective-fn state tree)]
    (if (or (nil? (:best-tree state))
            ((apply-min-or-max state) score (second (:best-tree state))))
      (assoc state :best-tree [tree score])
      state)))
