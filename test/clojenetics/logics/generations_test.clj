(ns clojenetics.logics.generations-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.generations :refer :all]))

(deftest raw-fitness-test
  (testing "should calculate Koza's raw fitness"
    (is (= 5 (raw-fitness-as-error 5 89)))))

(deftest standardized-fitness-test
  (testing "should calculate Koza's standardized fitness"
    (is (= 84 (standardized-fitness 5 89)))
    (is (= 5 (standardized-fitness 5 0)))
    (is (= 5 (standardized-fitness -5 0)))
    (is (= 5 (standardized-fitness 5)))
    (is (= 5 (standardized-fitness -5))))
  (testing "should always return something greater or equal to zero"
    (is (<= 0 (standardized-fitness -10)))
    (is (<= 0 (standardized-fitness 0)))
    (is (<= 0 (standardized-fitness 10)))
    (is (<= 0 (standardized-fitness -10 -10)))
    (is (<= 0 (standardized-fitness 0 0)))
    (is (<= 0 (standardized-fitness 10 10)))
    (is (<= 0 (standardized-fitness 10 -10)))
    (is (<= 0 (standardized-fitness 0 0)))
    (is (<= 0 (standardized-fitness -10 10)))))

(deftest adjusted-fitness-test
  (testing "should return a number between 0 and 1"
    (is (< 0 (adjusted-fitness 0)))
    (is (< 0 (adjusted-fitness 1)))
    (is (< 0 (adjusted-fitness 10)))
    (is (< 0 (adjusted-fitness 100)))))
