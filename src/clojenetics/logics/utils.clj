(ns clojenetics.logics.utils
  (:require [clojure.tools.logging :as log]))

(defn score-objective-fn [{:keys [target objective-fn] :as state} tree]
  (log/infof "Scoring objective fn with state %s and tree %s" state tree)
  (objective-fn target tree))

(defn apply-min-or-max [{:keys [min-or-max-objective-fn]}]
  (if (= min-or-max-objective-fn :minimize)
    <
    >))

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