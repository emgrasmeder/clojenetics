(ns integration.number-guess-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as clojenetics]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [clojure.tools.logging :as log]))

(def functions
  utils/math-operations)

(def terminals
  '[])

(def numbers
  [1 2 3 4 5 6 7 8 9 ])

(def tree-depth
  10)

(def target-number
  100)

(def population-size
  10)

(defn objective-fn [t fn]
  (utils/abs (- t (eval fn))))

(def state (-> {}
               (setters/set-terminals terminals)
               (setters/set-numbers numbers)
               (setters/set-functions functions)
               (setters/set-objective-fn objective-fn)
               (setters/set-tree-depth tree-depth)
               (setters/set-population-size population-size)
               (setters/set-target target-number)))

(deftest number-guess-test
  (testing "should return a single number representing the score"
    (let [result (:best-tree (clojenetics/grow state))]
      (is (number? (second result)))))
  (testing "should return a function or number representing the tree"
    (let [result (:best-tree (clojenetics/grow state))]
      (is (or (= clojure.lang.Cons (type (first result)))
              (= java.lang.Long (type (first result))))))))