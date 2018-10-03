(ns clojenetics.logics.generations-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.generations :as generations]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [bond.james :as bond]))

(deftest raw-fitness-test
  (testing "should calculate Koza's raw fitness"
    (is (= 84 (generations/raw-fitness-as-error 5 89)))))

(deftest standardized-fitness-test
  (testing "should calculate Koza's standardized fitness"
    (is (= 84 (generations/standardized-fitness 5 89)))
    (is (= 5 (generations/standardized-fitness 5 0)))
    (is (= 5 (generations/standardized-fitness -5 0)))
    (is (= 5 (generations/standardized-fitness 5)))
    (is (= 5 (generations/standardized-fitness -5))))
  (testing "should always return something greater or equal to zero"
    (is (<= 0 (generations/standardized-fitness -10)))
    (is (<= 0 (generations/standardized-fitness 0)))
    (is (<= 0 (generations/standardized-fitness 10)))
    (is (<= 0 (generations/standardized-fitness -10 -10)))
    (is (<= 0 (generations/standardized-fitness 0 0)))
    (is (<= 0 (generations/standardized-fitness 10 10)))
    (is (<= 0 (generations/standardized-fitness 10 -10)))
    (is (<= 0 (generations/standardized-fitness 0 0)))
    (is (<= 0 (generations/standardized-fitness -10 10)))))

(deftest adjusted-fitness-test
  (testing "should return a number between 0 and 1"
    (is (< 0 (generations/adjusted-fitness 0)))
    (is (< 0 (generations/adjusted-fitness 1)))
    (is (< 0 (generations/adjusted-fitness 10)))
    (is (< 0 (generations/adjusted-fitness 100)))))

(deftest do-generation-test
  (testing "should return state if no seeds remaining and no generations remaining"
    (is (= 0 (count (:population (generations/do-generation {:seeds-remaining 0 :generations-remaining 0}))))))
  (testing "should decrement the number of seeds remaining"
    (bond/with-stub!
      [[trees/create-tree (constantly '(+ 1 1))]]
      (is (= 0 (:seeds-remaining (generations/do-generation {:seeds-remaining 1 :generations-remaining 1}))))))
  (testing "should create a tree for each seed"
    (bond/with-stub!
      [[trees/create-tree (constantly '(+ 1 1))]
       [setters/set-scores (fn [& args] args)]]
      (is (= [{:tree '(+ 1 1)}
              {:tree '(+ 1 1)}
              {:tree '(+ 1 1)}]
             (:population (first (generations/do-generation {:seeds-remaining 3})))))))

  (testing "should evaluate fitness at the beginning of each generation"))

(deftest do-many-generations-test
  (testing "should decrement generations-remaining and create generations"
    (let [initial-state {:generations-remaining 1 :other-state-stuff 123}]
      (bond/with-stub!
        [[generations/do-generation (constantly (assoc initial-state :population [9 8 7]))]
         [setters/set-best-tree (fn [a] a)]]
        (is (= {:generations-remaining 0 :other-state-stuff 123 :population [9 8 7]}
               (generations/do-many-generations initial-state)))))))
