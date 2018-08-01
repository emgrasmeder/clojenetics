(ns clojenetics.logics.utils
  (:require [clojure.tools.logging :as log]))

(defn score-objective-fn [{:keys [target objective-fn]} tree]
  (log/infof "Scoring objective fn for tree %s" tree)
  (objective-fn target tree))

(defn abs "(abs n) is the absolute value of n" [n]
  (cond
    (not (number? n)) (throw (IllegalArgumentException.
                               "abs requires a number"))
    (neg? n) (- n)
    :else n))

(def math-operations
  ['[+ 2]
   '[- 2]
   '[* 2]
   '[/ 2]])

(def logical-operators
  ['[or 3]
   '[and 2]])