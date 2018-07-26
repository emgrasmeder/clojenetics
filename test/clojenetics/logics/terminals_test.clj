(ns clojenetics.logics.terminals-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.terminals :as terminals]))

(deftest try-for-terminal-test
  (testing "should return a terminal when there is no tree depth left"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal
             (terminals/try-for-terminal {:tree-depth 0

                                     :terminals  []
                                     :numbers    [the-only-terminal]
                                     :functions  []})))))

  (testing "should return a terminal when there are no units left in the population"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal
             (terminals/try-for-terminal {:tree-depth      100
                                     :population-size 0
                                     :terminals       []
                                     :numbers         [the-only-terminal]
                                     :functions       []}))))))