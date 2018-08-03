(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.populations :as populations]
            [clojenetics.logics.setters :as setters]))

(deftest create-tree-test
  (testing "should return original state if there's no room in the population left"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-subtree (constantly '(+ 2 2))]]
      (let [state {:tree-depth 2
                   :terminals  [[]]
                   :trees      [{:tree '(+ 1 1)}]
                   :numbers    [4 5 6]
                   :functions  [['+ 2]]}
            expected-tree {:tree-depth 2
                           :terminals  [[]]
                           :trees      [{:tree '(+ 1 1)}
                                        {:tree '(+ 2 2)}]
                           :numbers    [4 5 6]
                           :functions  [['+ 2]]}]
        (is (= expected-tree (trees/create-tree state)))))))

(deftest create-multiple-trees-test
  (testing "should return state if population is 0"
    (is (= 0 (count (:trees (trees/create-multiple-trees {:seeds-remaining 0}))))))
  (testing "should decrement the number of seeds remaining"
    (bond/with-stub!
      [[trees/create-tree (constantly {:seeds-remaining 1 :trees [{:tree '(+ 1 1)}]})]]
      (is (= 0 (:seeds-remaining (trees/create-multiple-trees {:seeds-remaining 1})))))))

