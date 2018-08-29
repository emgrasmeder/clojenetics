(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.setters :as setters]))

(deftest create-tree-test
  (testing "should return state with a new random tree"
    (bond/with-stub!
      [[terminals/try-for-terminal [(constantly false)
                                    (constantly 2)
                                    (constantly 2)]]]
      (let [state {:current-tree-depth 1
                   :functions          [['+ 2]]}
            expected-tree '(+ 2 2)]
        (is (= expected-tree (trees/create-tree state))))))
  (testing "should return just a terminal if no max-tree-depth left"
    (let [state {:current-tree-depth 1
                 :max-tree-depth     1
                 :numbers            [1]
                 :functions          [['+ 2]]}
          expected-tree '(+ 1 1)]
      (is (= expected-tree (trees/create-tree state))))))

(deftest do-generation-test
  (testing "should return state if no seeds remaining and no generations remaining"
    (is (= 0 (count (:trees (trees/do-generation {:seeds-remaining 0 :generations-remaining 0}))))))
  (testing "should decrement the number of seeds remaining"
    (bond/with-stub!
      [[trees/create-tree (constantly '(+ 1 1))]]
      (is (= 0 (:seeds-remaining (trees/do-generation {:seeds-remaining 1 :generations-remaining 1}))))))
  (testing "should create a tree for each seed"
    (bond/with-stub!
      [[trees/create-tree (constantly '(+ 1 1))]
         [setters/set-scores (fn [& args] args)]]
      (is (= [{:tree '(+ 1 1)}
                {:tree '(+ 1 1)}
                {:tree '(+ 1 1)}]
             (:trees (first (trees/do-generation {:seeds-remaining 3}))))))))

(deftest subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [tree '(+ (+ 1 2) 3)]
      (is (= tree (trees/subtree-at-index 0 tree)))
      (is (= '(+ 1 2) (trees/subtree-at-index 1 tree))))))

(deftest insert-subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [original-tree '(+ (+ 1 2) 3)]
      (is (= '(+ 1 1) (trees/insert-subtree-at-index 0 original-tree '(+ 1 1))))
      (is (= '(+ (- 100 10 1) 3) (trees/insert-subtree-at-index 1 original-tree '(- 100 10 1)))))))

(deftest do-many-generations-test
  (testing "should decrement generations-remaining and create generations"
    (bond/with-stub!
      [[trees/do-generation (constantly {:trees [9 8 7]})]]
      (is (= {:generations-remaining 0 :other-state-stuff 123 :population [9 8 7]}
             (trees/do-many-generations {:generations-remaining 1 :other-state-stuff 123}))))))
