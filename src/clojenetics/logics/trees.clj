(ns clojenetics.logics.trees
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]))

(declare create-tree)

(defn create-subtree [{:keys [functions tree-depth] :as state}]
  (let [[func arity] (rand-nth functions)
        state (setters/set-tree-depth state (dec (:tree-depth state)))]
    (log/infof "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [state]
  (log/infof "Doing create-tree with state: %s" state)
  (let [tree-or-terminal (or (terminals/try-for-terminal state)
                 (create-subtree state))]
    (setters/set-new-tree state tree-or-terminal)))

(declare create-multiple-trees)

(defn create-multiple-trees [state]
  (log/infof "Creating trees with state: %s" state)
  (if (not (zero? (:seeds-remaining state)))
    (-> state
        create-tree
        setters/dec-seeds-remaining
        create-multiple-trees)
    state))

(defn grow-trees [state]
  (create-tree state))