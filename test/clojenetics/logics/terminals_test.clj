(ns clojenetics.logics.terminals-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.terminals :as terminals]
            [bond.james :as bond]))

(deftest try-for-terminal-test
  (testing "should return a terminal when there is no tree depth left"
    (let [the-only-terminal 9999]
      (is (= the-only-terminal
             (terminals/try-for-terminal {:max-tree-depth 0

                                          :terminals  []
                                          :numbers    [the-only-terminal]
                                          :functions  []})))))

  (testing "should return a terminal when there are no seeds left"
    (let [the-only-terminal 9999]
      (bond/with-stub!
        [[rand (constantly 0.1)]]
        (is (= the-only-terminal
               (terminals/try-for-terminal {:max-tree-depth      100
                                            :seeds-remaining 0
                                            :terminals       []
                                            :numbers         [the-only-terminal]
                                            :functions       []})))))))