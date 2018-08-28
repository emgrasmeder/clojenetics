(ns clojenetics.logics.trees
  (:require [clojure.tools.logging :as log]
            [clojure.zip :as zip]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]))

(declare create-tree)

(defn create-subtree [{:keys [functions max-tree-depth] :as state}]
  (let [[func arity] (rand-nth functions)
        state (setters/dec-current-tree-depth state)]
    (log/infof "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [state]
  (log/infof "Doing create-tree with state: %s" state)
  (or (terminals/try-for-terminal state)
      (create-subtree state)))

(declare prepare-next-generation)
(declare generate-trees)

(defn prepare-next-generation [{:keys [generations initial-generations] :as state}]
  (if (zero? generations)
    (setters/set-scores state)
    (-> state setters/dec-generations generate-trees)))

(defn generate-trees [state]
  (log/infof "Creating trees with state: %s" state)
  (if-not (zero? (:seeds-remaining state))
    (let [tree (create-tree state)
          state (setters/dec-seeds-remaining state)
          state (setters/set-new-tree state tree)]
      (generate-trees state))
    (prepare-next-generation state)))

;; 1. Generate initial trees
;; 2. Live for first generation (get scores)
;; 3. Do next generation


; The following code from Lee Spencer at https://gist.github.com/lspector/3398614

(defn tree-depth [i tree]
  (mod (utils/abs i)
       (if (seq? tree)
         (count (flatten tree))
         1)))

(defn subtree-at-index
  [index tree]
  (log/infof "Getting subtree from tree %s at index %s" tree index)
  (let [index (tree-depth index tree)
        zipper (zip/seq-zip tree)]
    (loop [z zipper i index]
      (if (zero? i)
        (zip/node z)
        (if (seq? (zip/node z))
          (recur (zip/next (zip/next z)) (dec i))
          (recur (zip/next z) (dec i)))))))

(defn insert-subtree-at-index
  "Returns a copy of tree with the subtree formerly indexed by
point-index (in a depth-first traversal) replaced by new-subtree."
  [index tree new-subtree]
  (let [index (tree-depth index tree)
        zipper (zip/seq-zip tree)]
    (loop [z zipper i index]
      (if (zero? i)
        (zip/root (zip/replace z new-subtree))
        (if (seq? (zip/node z))
          (recur (zip/next (zip/next z)) (dec i))
          (recur (zip/next z) (dec i)))))))
