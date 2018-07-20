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
    (is (= {:objective-fn ['(fn [])]} (core/set-objective-fn {} ['(fn [])])))))

(deftest set-generation-limit-test
  (testing "sets generation-limit in the program state"
    (is (= {:generation-limit 1} (core/set-generation-limit {} 1)))))

(deftest try-for-terminal-test
  (testing "should return a terminal when there are no generations left"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal (core/try-for-terminal {:generation-limit 0
                                                       :terminals        []
                                                       :numbers          [the-only-terminal]
                                                       :functions        []})))))
  (testing "return a function"
    (println "starting test")
    (bond/with-stub!
      [[core/try-for-terminal [(fn [& args] false)
                               (fn [& args] 1)
                               (fn [& args] 1)]]]
      (is (= 2 (eval (core/create-tree {:generation-limit 2
                                          :terminals        [[]]
                                          :numbers          [1]
                                          :functions        [['+ 2]]})))))))