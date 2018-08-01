(ns clojenetics.logics.setters-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.utils :as utils]))

(deftest set-terminals-test
  (testing "sets terminals in the program state"
    (is (= {:terminals [1]} (setters/set-terminals {} [1])))))

(deftest set-numbers-test
  (testing "sets numbers in the program state"
    (is (= {:numbers [1]} (setters/set-numbers {} [1])))))

(deftest set-functions-test
  (testing "sets functions in the program state"
    (is (= {:functions [['+ 2]]} (setters/set-functions {} [['+ 2]])))))

(deftest set-objective-fn-test
  (testing "sets objective-fn in the program state"
    (is (= {:objective-fn ['(fn [])]} (setters/set-objective-fn {} ['(fn [])])))))

(deftest set-target-test
  (testing "sets target in the program state"
    (is (= {:target 100} (setters/set-target {} 100)))))

(deftest set-tree-depth-test
  (testing "sets tree-depth in the program state"
    (is (= {:tree-depth 1} (setters/set-tree-depth {} 1)))))

(deftest set-best-tree-test
  (testing "sets tree-depth in the program state"
    (is (= {:best-tree [1 10]} (setters/set-best-tree {} [1 10])))))

(deftest set-new-tree-test
  (testing "sets new tree in tree attribute"
    (let [state {:trees [{:tree '[+ 1 1] :score 10}]}]
      (is (= [{:tree  '[+ 1 1] :score 10} {:tree '[+ 4 4]}]
             (:trees (setters/set-new-tree state '[+ 4 4])))))))

(deftest set-scores-test
  (testing "should take in a state with a tree and objective function and enrich with a scores"
    (let [state {:target       50
                 :trees        [{:tree '(+ 100 0)}
                                {:tree '(+ 0 0)}]
                 :objective-fn (fn [t fn]
                                 (utils/abs (- t (eval fn))))}]
      (is (= [{:tree '(+ 100 0)
               :score 50}
              {:tree '(+ 0 0)
               :score 50}] (:trees (setters/set-scores state)))))))