(ns clojenetics.logics.trees
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]))

(declare create-tree)

(defn create-subtree [{:keys [functions max-tree-depth] :as state}]
  (let [[func arity] (rand-nth functions)
        state (setters/set-max-tree-depth state (dec (:max-tree-depth state)))]
    (log/infof "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [state]
  (log/infof "Doing create-tree with state: %s" state)
  (or (terminals/try-for-terminal state)
      (create-subtree state)))


(declare create-multiple-trees)

(defn create-multiple-trees [state]
  (log/infof "Creating trees with state: %s" state)
  (if (not (zero? (:seeds-remaining state)))
    (let [tree (create-tree state)
          state (setters/dec-seeds-remaining state)
          state (setters/set-new-tree state tree)]
      (create-multiple-trees state))
    (setters/set-scores state)))