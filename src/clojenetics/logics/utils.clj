(ns clojenetics.logics.utils
  (:require [taoensso.timbre :refer [debug debugf]]))

(defn score-objective-fn [{:keys [target objective-fn]} tree]
  (debugf "Scoring objective fn for tree %s" tree)
  (objective-fn target tree))

(defn strictly-positive? [num]
  (> num 0))

(defn abs "(abs n) is the absolute value of n" [n]
  (cond
    (not (number? n)) (throw (IllegalArgumentException.
                               "abs requires a number"))
    (neg? n) (- n)
    :else n))

(def math-operations
  [
   '[+ 2]
   '[- 2]
   '[* 2]
   ])

(def logical-operators
  [
   '[or 2]
   '[and 2]
   '[if 2]
   ])

(def basic-functions
  (concat math-operations logical-operators))

(def basic-terminals
  '[])

(def basic-numbers
  [1 2 3 4 5 6 7 8 9 0])