(ns clojenetics.trees
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]))

(declare create-tree)

(defn create-subtree [{:keys [functions tree-depth] :as state}]
  (let [[func arity] (rand-nth functions)
        state (setters/set-tree-depth state (dec (:tree-depth state)))]
    (log/debugf "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [state]
  (or (terminals/try-for-terminal state)
      (create-subtree state)))

(defn get-best-tree [state]
  (log/debug "Getting best tree with state: " state)
  (let [tree (create-tree state)
        score (utils/score-objective-fn state tree)]
    (if (or (nil? (:best-tree state))
            ((utils/apply-min-or-max state) score (second (:best-tree state))))
      (assoc state :best-tree [tree score])
      state)))
