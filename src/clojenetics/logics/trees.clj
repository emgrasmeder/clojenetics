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
  (or (terminals/try-for-terminal state)
      (create-subtree state)))

(defn tree-has-better-score [state score]
  (if (= (:min-or-max-objective-fn state) :minimize)
    (< score (second (:best-tree state)))
    (> score (second (:best-tree state)))))

(defn try-to-update-best-tree [state tree score]
  (if (or (nil? (:best-tree state))
          (tree-has-better-score state score))
    (setters/set-best-tree state [tree score])
    state))

(defn get-best-tree [state]
  (log/info "Getting best tree with state: " state)
  (let [tree (create-tree state)
        score (utils/score-objective-fn state tree)]
    (try-to-update-best-tree state tree score)))
