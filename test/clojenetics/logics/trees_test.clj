(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.populations :as populations]
            [clojenetics.logics.setters :as setters]))

(deftest create-tree-test
  (testing "should return state with a trees attribute"
    (bond/with-stub!
      [[populations/population-allowance (constantly true)]
       [terminals/try-for-terminal [(constantly false)
                                    (constantly 1)
                                    (constantly 1)]]]
      (is (= '[+ 1 1]
             (trees/create-tree {:tree-depth 2
                                 :terminals  [[]]
                                 :trees      []
                                 :numbers    [1 2 3]
                                 :functions  [['+ 2]]})))))
  (testing "should return original state if there's no room in the population left"
    (bond/with-stub!
      [[populations/population-allowance (constantly false)]
       [terminals/try-for-terminal [(constantly false)
                                    (constantly 1)
                                    (constantly 1)]]]
      (is (= '[+ 1 1]
             (trees/create-tree {:tree-depth 2
                                 :terminals  [[]]
                                 :trees      ['[+ 1 1]]
                                 :numbers    [4 5 6]
                                 :functions  [['+ 2]]}))))))

(deftest try-to-update-best-tree-test
  (testing "should set best tree if there's no current best tree"
    (bond/with-stub!
      [[setters/set-best-tree (constantly 0)]]
      (trees/try-to-update-best-tree {:best-tree nil} 0 0)
      (is (= 1 (-> setters/set-best-tree bond/calls count)))))
  (testing "should set new best tree if new score is better"
    (bond/with-stub!
      [[setters/set-best-tree (constantly 0)]
       [trees/tree-has-better-score (constantly true)]]
      (trees/try-to-update-best-tree {:best-tree true :min-or-max-objective-fn :minimize} 0 0)
      (is (= 1 (-> setters/set-best-tree bond/calls count))))))

(deftest tree-has-better-score-test
  (testing "should return true if minimize function score is smaller score in state"
    (is (true? (trees/tree-has-better-score {:min-or-max-objective-fn :minimize
                                             :best-tree               [:blah 100]}
                                            0))))
  (testing "should return true if maximize function score is larger score in state"
    (is (true? (trees/tree-has-better-score {:min-or-max-objective-fn :maximize
                                             :best-tree               [:blah 0]}
                                            100)))))
