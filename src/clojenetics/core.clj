(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.setters :as setters]))

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
        state (setters/set-tree-depth state (dec (:tree-depth state)))]
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
