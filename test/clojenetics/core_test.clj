(ns clojenetics.core-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as core]
            [bond.james :as bond]))

(deftest set-terminals-test
  (testing "sets terminals in the program state"
    (is (= {:terminals [1]} (core/set-terminals {} [1])))))

(deftest set-numbers-test
  (testing "sets numbers in the program state"
    (is (= {:numbers [1]} (core/set-numbers {} [1])))))

(deftest set-functions-test
  (testing "sets functions in the program state"
    (is (= {:functions [['+ 2]]} (core/set-functions {} [['+ 2]])))))

(deftest set-objective-fn-test
  (testing "sets objective-fn in the program state"
    (is (= {:objective-fn ['(fn [] )]} (core/set-objective-fn {} ['(fn [])])))))

(deftest set-generation-limit-test
  (testing "sets generation-limit in the program state"
    (is (= {:generation-limit 1} (core/set-generation-limit {} 1)))))
