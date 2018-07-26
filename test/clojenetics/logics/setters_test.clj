(ns clojenetics.logics.setters-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.setters :as setters]))

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
