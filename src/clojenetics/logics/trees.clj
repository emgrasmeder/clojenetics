(ns clojenetics.logics.trees
  (:require [clojure.tools.logging :as log]
            [clojure.zip :as zip]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :refer [abs strictly-positive?]]))

(declare create-tree)

(defn create-random-subtree [{:keys [functions] :as state}]
  (let [[func arity] (rand-nth functions)]
    (log/debugf "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [{:keys [propagation-technique] :as state}]
  (log/debugf "Doing create-tree with state: %s" state)
  (if (or (nil? propagation-technique)
          (= propagation-technique :random))
    (or (terminals/try-for-terminal state)
        (create-random-subtree (setters/dec-current-tree-depth state)))))

; The following code from Lee Spencer at https://gist.github.com/lspector/3398614

(defn tree-depth [i tree]
  (mod (abs i)
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
