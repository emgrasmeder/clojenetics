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
      (is (= expected-tree (trees/create-tree state)))))

  (testing "should create-random-subtree if there is no propagation technique"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-random-subtree (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique nil
                          :trees ['(+ 2 2)]})
      (is (= 1 (-> trees/create-random-subtree bond/calls count)))))
  (testing "should create-random-subtree if propagation technique is :random"
      (bond/with-stub!
        [[terminals/try-for-terminal (constantly false)]
         [trees/create-random-subtree (constantly {})]
         [setters/dec-current-tree-depth (constantly {})]]
        (trees/create-tree {:propagation-technique :random
                            :trees ['(+ 2 2)]})
        (is (= 1 (-> trees/create-random-subtree bond/calls count)))))
  (testing "should not create-random-subtree if there are no trees in the state yet"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-random-subtree (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :something
                          :trees ['(+ 2 2)]})
      (is (= 0 (-> trees/create-random-subtree bond/calls count)))))
  (testing "should create-random-subtree if there are no trees in the state yet"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-random-subtree (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :something})
      (is (= 1 (-> trees/create-random-subtree bond/calls count)))))

  (testing "should create tree according to propagation technique, if not random"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-subtree-by-mutation (constantly {})]
       [trees/create-subtree-by-permutation (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :mutation
                          :trees ['(+ 2 2)]})
      (is (= 1 (-> trees/create-subtree-by-mutation bond/calls count)))
      (trees/create-tree {:propagation-technique :permutation
                          :trees ['(+ 2 2)]})
      (is (= 1 (-> trees/create-subtree-by-permutation bond/calls count))))))

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
