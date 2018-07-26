(ns clojenetics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.populations :as populations]))

(deftest get-best-tree-test
  (bond/with-stub!
    [[trees/create-tree (constantly '(+ 100 0))]
     [utils/score-objective-fn (constantly 0)]]
    (testing "should return state with the only tree if no more population allowed"
      (let [state {:best-tree ['(+ 100 0) 0]}]
        (is (= state (trees/get-best-tree state)))))
    (testing "should return state with new best-tree if there was no tree before"
      (let [state {}]
        (is (= (assoc state :best-tree ['(+ 100 0) 0]) (trees/get-best-tree state)))))
    (testing "should return state with new best-tree if a new best tree is found"
      (let [state {:min-or-max-objective-fn :minimize
                   :best-tree ['(+ 100 0) 10]}]
        (is (= (assoc state :best-tree ['(+ 100 0) 0]) (trees/get-best-tree state)))))
    (testing "should consider if objective-fn is a min or max function"
      (let [state {:min-or-max-objective-fn :maximize
                   :best-tree               ['(+ 100 0) 10]}]
        (is (= state (trees/get-best-tree state)))))))



(deftest create-tree-test
  (testing "should return a function"
    (bond/with-stub!
      [[populations/population-allowance (constantly true)]
       [terminals/try-for-terminal [(constantly false)
                               (constantly 1)
                               (constantly 1)]]]
      (is (= 2
             (eval (trees/create-tree {:tree-depth 2
                                      :terminals   [[]]
                                      :numbers     [1]
                                      :functions   [['+ 2]]}))))))
  (testing "should return original state if there's no room in the population left"
    (bond/with-stub!
      [[populations/population-allowance (constantly false)]
       [terminals/try-for-terminal [(constantly false)
                               (constantly 1)
                               (constantly 1)]]]
      (is (= 2
             (eval (trees/create-tree {:tree-depth 2
                                      :terminals   [[]]
                                      :numbers     [1]
                                      :functions   [['+ 2]]})))))))

