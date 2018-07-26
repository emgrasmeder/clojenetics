(ns clojenetics.core-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as core]
            [bond.james :as bond]))

(deftest get-best-tree-test
  (bond/with-stub!
    [[core/create-tree (constantly '(+ 100 0))]
     [core/score-objective-fn (constantly 0)]]
    (testing "should return state with the only tree if no more population allowed"
      (let [state {:best-tree ['(+ 100 0) 0]}]
        (is (= state (core/get-best-tree state)))))
    (testing "should return state with new best-tree if there was no tree before"
      (let [state {}]
        (is (= (assoc state :best-tree ['(+ 100 0) 0]) (core/get-best-tree state)))))
    (testing "should return state with new best-tree if a new best tree is found"
      (let [state {:min-or-max-objective-fn :minimize
                   :best-tree ['(+ 100 0) 10]}]
        (is (= (assoc state :best-tree ['(+ 100 0) 0]) (core/get-best-tree state)))))
    (testing "should consider if objective-fn is a min or max function"
      (let [state {:min-or-max-objective-fn :maximize
                   :best-tree               ['(+ 100 0) 10]}]
        (is (= state (core/get-best-tree state)))))))

(deftest solve-objective-fn-test
  (testing "should evaluate tree against objective function"
    (let [state {:target       100
                 :objective-fn (fn [t fn]
                                 (Math/abs (- t (eval fn))))}]
      (is (= 0 (core/score-objective-fn state '(+ 100 0)))))
    (let [state {:target       200
                 :objective-fn (fn [t fn]
                                 (Math/abs (- t (eval fn))))}]
      (is (= 100 (core/score-objective-fn state '(+ 100 0)))))))

(deftest create-tree-test
  (testing "should return a function"
    (bond/with-stub!
      [[core/population-allowance (constantly true)]
       [core/try-for-terminal [(constantly false)
                               (constantly 1)
                               (constantly 1)]]]
      (is (= 2
             (eval (core/create-tree {:tree-depth 2
                                      :terminals  [[]]
                                      :numbers    [1]
                                      :functions  [['+ 2]]}))))))
  (testing "should return original state if there's no room in the population left"
    (bond/with-stub!
      [[core/population-allowance (constantly false)]
       [core/try-for-terminal [(constantly false)
                               (constantly 1)
                               (constantly 1)]]]
      (is (= 2
             (eval (core/create-tree {:tree-depth 2
                                      :terminals  [[]]
                                      :numbers    [1]
                                      :functions  [['+ 2]]})))))))

(deftest try-for-terminal-test
  (testing "should return a terminal when there is no tree depth left"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal
             (core/try-for-terminal {:tree-depth 0
                                     :terminals  []
                                     :numbers    [the-only-terminal]
                                     :functions  []})))))
  (testing "should return a terminal when there are no units left in the population"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal
             (core/try-for-terminal {:tree-depth      100
                                     :population-size 0
                                     :terminals       []
                                     :numbers         [the-only-terminal]
                                     :functions       []}))))))

(deftest population-allows-test
  (testing "should return true if population is greater than 0"
    (is (core/population-allowance {:population-size 1})))

  (testing "should return false if population is less than or equal to 0"
    (is (not (core/population-allowance {:population-size 0})))))