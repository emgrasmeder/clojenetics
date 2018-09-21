(ns clojenetics.logics.trees
  (:require [clojure.tools.logging :as log]
            [clojure.zip :as zip]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :refer [abs strictly-positive?]]))

;; For now, selecting trees for (per)mutation will be according to uniform-random distributions
(defn create-subtree-by-mutation [state]
  state)

(declare create-tree)
(declare create-subtree-by-permutation)
(defn create-random-subtree [{:keys [functions] :as state}]
  (let [[func arity] (rand-nth functions)]
    (log/debugf "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

;; TODO: This is where we can do #7, and mutate trees
(defn create-tree [{:keys [propagation-technique] :as state}]
  (printf "Doing create-tree with state: %s" state)
  (if (or (nil? propagation-technique)
          (= propagation-technique :random)
          (empty? (:trees state)))
    (or (terminals/try-for-terminal state)
        (create-random-subtree (setters/dec-current-tree-depth state)))
    (case propagation-technique
      :mutation (create-subtree-by-mutation state)
      :permutation (create-subtree-by-permutation state)
      state)))

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
  (log/infof "Inserting subtree %s into tree %s at index %s" new-subtree tree index)
  (let [index (tree-depth index tree)
        zipper (zip/seq-zip tree)]
    (loop [z zipper i index]
      (if (zero? i)
        (zip/root (zip/replace z new-subtree))
        (if (seq? (zip/node z))
          (recur (zip/next (zip/next z)) (dec i))
          (recur (zip/next z) (dec i)))))))
;; End borrowed code block


(defn random-subtree [tree]
  (log/info "Making random subtree")
  (let [index (rand-int (count (flatten tree)))
        subtree (subtree-at-index index tree)]
    (if (list? subtree)
      [index subtree]
      (random-subtree tree))))

(defn permute-branches [tree]
  (log/info "Permuting subtree's branches")
  (cons (first tree) (shuffle (rest tree))))

(defn create-subtree-by-permutation [{:keys [trees] :as state}]
  ;; TODO: select in other ways besides uniform random
  (log/info "Permuting tree")
  (let [tree (:tree (rand-nth trees))
        [index subtree] (random-subtree tree)
        shuffled-subtree (permute-branches subtree)]
    (insert-subtree-at-index index tree shuffled-subtree)))


(defn sort-trees-by-adjusted-fitness [{:keys [trees] :as state}]
  "Adjusted fitness assumes bigger score is better"
  (assoc state :trees (sort-by :score > trees)))