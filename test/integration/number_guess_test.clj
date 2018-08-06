(ns integration.number-guess-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as clojenetics]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [clojure.tools.logging :as log]))

(def functions
  ['[+ 2]
   '[- 2]
   '[* 2]
   '[if 3]])

(def terminals
  '[])

(def numbers
  [1 2 3 4 5 6 7 8 9 ])

(def max-tree-depth
  6)

(def target-number
  100)

(def num-seeds
  20)

(defn objective-fn [t fn]
  (utils/abs (- t (eval fn))))

(def state (-> {}
               (setters/set-terminals terminals)
               (setters/set-numbers numbers)
               (setters/set-functions functions)
               (setters/set-objective-fn objective-fn)
               (setters/set-max-tree-depth max-tree-depth)
               (setters/set-seed-count num-seeds)
               (setters/set-target target-number)))

(deftest number-guess-test
  #_(testing "should return a state object"
    (let [result (trees/create-multiple-trees state)]
      (is (= 123 (:trees result)))))
  #_(testing "should return a function or number representing the tree"
    (let [result (:best-tree (clojenetics/grow state))]
      (is (or (= clojure.lang.Cons (type (first result)))
              (= java.lang.Long (type (first result))))))))