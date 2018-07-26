(ns clojenetics.logics.populations-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.populations :as populations]))

(deftest population-allows-test
  (testing "should return true if population is greater than 0"
    (is (populations/population-allowance {:population-size 1})))

  (testing "should return false if population is less than or equal to 0"
    (is (not (populations/population-allowance {:population-size 0})))))