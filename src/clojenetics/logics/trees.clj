(ns clojenetics.logics.trees
  (:require [taoensso.timbre :refer [debug debugf]]
            [clojure.zip :as zip]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :refer [abs strictly-positive?]]))

(defn create-tree-by-mutation [state]
  state)

(declare create-tree)
(declare create-tree-by-permutation)

(defn create-random-subtree [{:keys [functions] :as state}]
  (let [[func arity] (rand-nth functions)]
    (debugf "Recursing tree creation with state: %s" state)
    (cons func (repeatedly arity #(create-tree state)))))

(defn create-tree [{:keys [propagation-technique] :as state}]
  (debug "Doing create-tree with state: " state)
  (if (or (nil? propagation-technique)
          (= propagation-technique :random)
          (empty? (:population state)))
    (or (terminals/try-for-terminal state)
        (create-random-subtree (setters/dec-current-tree-depth state)))
    (case propagation-technique
      :mutation (create-tree-by-mutation (setters/dec-current-tree-depth state))
      :permutation (create-tree-by-permutation (setters/dec-current-tree-depth state))
      state)))

; The following code from Lee Spencer at https://gist.github.com/lspector/3398614
(defn tree-depth [i tree]
  (mod (abs i)
       (if (seq? tree)
         (count (flatten tree))
         1)))

(defn subtree-at-index
  [index tree]
  (debugf "Getting subtree from tree %s at index %s" tree index)
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
  (debugf "Inserting subtree %s into tree %s at index %s" new-subtree tree index)
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
  (debug "Making random subtree")
  (let [index (rand-int (count (flatten tree)))
        subtree (subtree-at-index index tree)]
    (debugf "Got subtree %s of type %s" subtree (type subtree))
    (if (or (= clojure.lang.PersistentList (type subtree))
            (= clojure.lang.Cons (type subtree)))
      [index subtree]
      (random-subtree tree))))

(defn permute-branches [tree]
  (debug "Permuting subtree's branches")

  (cons (first tree) (shuffle (rest tree))))

(defn sort-trees-by-adjusted-fitness [{:keys [population] :as state}]
  "Adjusted fitness assumes bigger score is better"
  (assoc state :population (sort-by :score > population)))

(defn get-tree-cooresponding-to-score [state]
  ;; TODO: Only do sum once per generation
  (let [state (setters/sum-of-scores state)
        candidate-tree (rand-nth (:population state))]
    (if (or (nil? (:sum-of-scores state))
            (zero? (:sum-of-scores state)))
      (do (debug "returning candidate tree " candidate-tree)
          candidate-tree)
      (if (> (:score candidate-tree) (rand (:sum-of-scores state)))
        (get-tree-cooresponding-to-score state)
        candidate-tree))))

(defn create-tree-by-permutation [state]
  (debug "Permuting tree from state" state)
  (let [tree (:tree (get-tree-cooresponding-to-score state))
        [index subtree] (random-subtree tree)
        shuffled-subtree (permute-branches subtree)]
    (insert-subtree-at-index index tree shuffled-subtree)))