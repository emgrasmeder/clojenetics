(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.setters :as setters]))

(deftest create-tree-test
  #_(testing "should return state with a new random tree"
    (bond/with-stub!
      [[terminals/try-for-terminal [(constantly false)
                                    (constantly 2)
                                    (constantly 2)]]]
      (let [state {:current-tree-depth 1
                   :functions  [['+ 2]]}
            expected-tree '(+ 2 2)]
        (is (= expected-tree (trees/create-tree state))))))
  (testing "should return just a terminal if no max-tree-depth left"
    (let [state {:current-tree-depth 1
                 :max-tree-depth 1
                 :numbers [1]
                 :functions  [['+ 2]]}
          expected-tree '(+ 1 1)]
      (is (= expected-tree (trees/create-tree state))))))

(deftest create-multiple-trees-test
  (bond/with-stub!
    [[utils/score-objective-fn (constantly 1)]]
    (testing "should return state if no seeds remaining"
      (is (= 0 (count (:trees (trees/create-multiple-trees {:seeds-remaining 0}))))))
    (testing "should decrement the number of seeds remaining"
      (bond/with-stub!
        [[trees/create-tree (constantly '(+ 1 1))]]
        (is (= 0 (:seeds-remaining (trees/create-multiple-trees {:seeds-remaining 1}))))))
    (testing "should create a tree for each seed"
      (bond/with-stub!
        [[trees/create-tree (constantly '(+ 1 1))]]
        (is (= 10 (count (:trees (trees/create-multiple-trees {:seeds-remaining 10})))))))))

