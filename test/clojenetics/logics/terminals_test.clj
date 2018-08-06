(ns clojenetics.logics.terminals-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.terminals :as terminals]
            [bond.james :as bond]))

(deftest try-for-terminal-test
  (testing "should return a terminal when there is no tree depth left"
    (let [the-only-terminal 9999]
      (bond/with-stub!
        [[rand (constantly 0.4)]]
        (is (= the-only-terminal
               (terminals/try-for-terminal {:current-tree-depth 0
                                            :terminals          []
                                            :numbers            [the-only-terminal]
                                            :functions          []}))))))
  (testing "should return false when rand throws less than 0.5"
    (let [the-only-terminal 9999]
      (bond/with-stub!
        [[rand (constantly 0.2)]]
        (is (= false
               (terminals/try-for-terminal {:current-tree-depth 10
                                            :terminals          []
                                            :numbers            [the-only-terminal]
                                            :functions          []}))))))

  (testing "should return false when there is tree depth > 0"
    (bond/with-stub!
      [[rand (constantly 0.2)]]
      (is (= false
             (terminals/try-for-terminal {:current-tree-depth 100})))))
  (testing "should return false when max and current tree depth are equal"
    (is (= false
           (terminals/try-for-terminal {:current-tree-depth 10
                                        :max-tree-depth     10})))))