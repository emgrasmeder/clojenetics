(ns clojenetics.logics.setters-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.generations :as generations]))

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

(deftest set-max-tree-depth-test
  (testing "sets max-tree-depth in the program state"
    (is (= 1 (:max-tree-depth (setters/set-max-tree-depth {} 1)))))
  (testing "sets current-tree-depth in the program state"
    (is (= 1 (:current-tree-depth (setters/set-max-tree-depth {} 1))))))

(deftest dec-seeds-remaining-test
  (testing "decrements seeds remaining"
    (is (= {:seeds-remaining 0}
           (setters/dec-seeds-remaining {:seeds-remaining 1})))))

(deftest set-best-tree-test
  (testing "sets max-tree-depth in the program state"
    (let [state {:min-or-max-objective-fn :minimize
                 :trees                   [{:score 10}]}]
      (is (= {:score 10}
             (:best-tree (setters/set-best-tree state)))))))

(deftest set-new-tree-test
  (testing "sets new tree in tree attribute"
    (let [state {:trees [{:tree '[+ 1 1] :score 10}]}]
      (is (= [{:tree '[+ 1 1] :score 10} {:tree '[+ 4 4]}]
             (:trees (setters/set-new-tree state '[+ 4 4]))))))
  (testing "sets tree even when none exist"
    (let [state {}]
      (is (= [{:tree '[+ 4 4]}]
             (:trees (setters/set-new-tree state '[+ 4 4])))))))

(deftest set-scores-test
  (testing "should take in a state with a tree and
  objective function and enrich with a scores"
    (let [obj-fn (fn [target fn] (generations/standardized-fitness (eval fn) target))
          state {:target       50
                 :trees        [{:tree '(+ 100 0)}
                                {:tree '(+ 0 0)}]
                 :objective-fn obj-fn}]
      (is (= [{:tree  '(+ 100 0)
               :score 50}
              {:tree  '(+ 0 0)
               :score 50}] (:trees (setters/set-scores state)))))))

(deftest dec-current-tree-depth-test
  (testing "decrements current-tree-depth remaining"
    (is (= {:current-tree-depth 0}
           (setters/dec-current-tree-depth {:current-tree-depth 1})))))

(deftest set-generations-test
  (testing "sets set with generations"
    (let [state {}]
      (is (= {:generations-remaining 10}) (setters/set-generations state 10)))))

(deftest dec-generations-test
  (testing "should decrement generations"
    (is (= {:generations-remaining 0}
           (setters/dec-generations {:generations-remaining 1})))))

(deftest set-population-test
  (testing "should set population"
    (is (= {:population [1 2 3]} (setters/set-population {} [1 2 3])))))

(deftest sum-of-scores-test
  (testing "should sum the scores of trees"
    (is (= 10 (:sum-of-scores (setters/sum-of-scores
                                {:trees [{:score 5 :tree '()}
                                         {:score 5 :tree '()}]}))))
    (testing "should sum the scores of trees"
      (is (= 20 (:sum-of-scores (setters/sum-of-scores
                                  {:trees [{:score 5 :tree '()}
                                           {:score 5 :tree '()}
                                           {:score 5 :tree '()}
                                           {:score 5 :tree '()}]})))))))
