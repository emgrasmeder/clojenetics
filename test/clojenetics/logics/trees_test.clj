(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.setters :as setters]))

(deftest create-tree-test
  (testing "should return state with a new random tree"
    (bond/with-stub!
      [[terminals/try-for-terminal [(constantly false)
                                    (constantly 2)
                                    (constantly 2)]]]
      (let [state {:current-tree-depth 1
                   :functions          [['+ 2]]}
            expected-tree '(+ 2 2)]
        (is (= expected-tree (trees/create-tree state))))))
  (testing "should return just a terminal if no max-tree-depth left"
    (let [state {:current-tree-depth 1
                 :max-tree-depth     1
                 :numbers            [1]
                 :functions          [['+ 2]]}
          expected-tree '(+ 1 1)]
      (is (= expected-tree (trees/create-tree state))))))

(deftest subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [tree '(+ (+ 1 2) 3)]
      (is (= tree (trees/subtree-at-index 0 tree)))
      (is (= '(+ 1 2) (trees/subtree-at-index 1 tree))))))

(deftest insert-subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [original-tree '(+ (+ 1 2) 3)]
      (is (= '(+ 1 1) (trees/insert-subtree-at-index 0 original-tree '(+ 1 1))))
      (is (= '(+ (- 100 10 1) 3) (trees/insert-subtree-at-index 1 original-tree '(- 100 10 1)))))))
