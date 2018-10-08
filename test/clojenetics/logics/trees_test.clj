(ns clojenetics.logics.trees-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.trees :as trees]
            [bond.james :as bond]
            [clojenetics.logics.terminals :as terminals]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.generations :as generations]))

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
                          :population            ['(+ 2 2)]})
      (is (= 1 (-> trees/create-random-subtree bond/calls count)))))
  (testing "should create-random-subtree if propagation technique is :random"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-random-subtree (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :random
                          :population            ['(+ 2 2)]})
      (is (= 1 (-> trees/create-random-subtree bond/calls count)))))
  (testing "should not create-random-subtree if there are no trees in the state yet"
    (bond/with-stub!
      [[terminals/try-for-terminal (constantly false)]
       [trees/create-random-subtree (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :something
                          :population            ['(+ 2 2)]})
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
       [trees/create-tree-by-mutation (constantly {})]
       [trees/create-tree-by-permutation (constantly {})]
       [setters/dec-current-tree-depth (constantly {})]]
      (trees/create-tree {:propagation-technique :mutation
                          :population            ['(+ 2 2)]})
      (is (= 1 (-> trees/create-tree-by-mutation bond/calls count)))
      (trees/create-tree {:propagation-technique :permutation
                          :population            ['(+ 2 2)]})
      (is (= 1 (-> trees/create-tree-by-permutation bond/calls count))))))

(deftest subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [tree '(+ (+ 1 2) (+ 3 (+ 4 5)))]
      (is (= tree (trees/subtree-at-index 0 tree)))
      (is (= '(+ 1 2) (trees/subtree-at-index 1 tree)))
      (is (= 1 (trees/subtree-at-index 2 tree)))
      (is (= 2 (trees/subtree-at-index 3 tree)))
      (is (= '(+ 3 (+ 4 5)) (trees/subtree-at-index 4 tree)))
      (is (= 3 (trees/subtree-at-index 5 tree)))
      (is (= '(+ 4 5) (trees/subtree-at-index 6 tree)))
      (is (= 4 (trees/subtree-at-index 7 tree)))
      (is (= 5 (trees/subtree-at-index 8 tree))))))

(deftest insert-subtree-at-index-test
  (testing "should return a subtree of tree t at index i"
    (let [original-tree '(+ (+ 1 2) 3)]
      (is (= '(+ 1 1) (trees/insert-subtree-at-index 0 original-tree '(+ 1 1))))
      (is (= '(+ (- 100 10 1) 3) (trees/insert-subtree-at-index 1 original-tree '(- 100 10 1)))))))

(deftest create-tree-by-permutation-test
  (testing "should reverse arguments in a given tree"
    (bond/with-stub! [[trees/get-tree-cooresponding-to-score (constantly {:tree '(+ 1 (+ 2 3))})]]
                     (let [original-state (setters/set-new-tree {} '(+ 1 (+ 2 3)))
                           modified-state (trees/create-tree-by-permutation original-state)]
                       (is (= 1 (count (filter true? [(= '(+ 1 (+ 2 3)) modified-state)
                                                      (= '(+ (+ 2 3) 1) modified-state)
                                                      (= '(+ 1 (+ 3 2)) modified-state)])))))))

  (testing "should permute trees given a complete state"
    (let [original-state {:generations-remaining   5
                          :terminals               []
                          :propagation-technique   :permutation
                          :objective-fn            generations/raw-fitness-as-error
                          :numbers                 [1 2 3 4 5 6 7 8 9 0]
                          :seeds-remaining         4
                          :functions               [['+ 2] ['- 2] ['* 2] ['/ 2] ['or 2] ['and 2] ['if 2]]
                          :min-or-max-objective-fn :minimize
                          :current-tree-depth      1
                          :population              [{:tree '(+ (or 2 3) (or 1 0))}]
                          :target                  123
                          :max-tree-depth          2}
          modified-state (trees/create-tree-by-permutation original-state)]
      (is (= 1 1)))))

(deftest random-subtree-test
  (testing "subtree should have function at first (zeroeth) index"
    (is (= [0 '(+ 1 2)] (trees/random-subtree '(+ 1 2))))
    (let [rand-subtree (trees/random-subtree '(+ 1 (+ 2 (+ 3 4))))]
      (is (some? (or (= [0 '(+ 1 (+ 2 (+ 3 4)))] rand-subtree)
                     (= [2 '(+ 2 (+ 3 4))] rand-subtree)
                     (= [4 '(+ 3 4)] rand-subtree))))))

  (testing "subtree will be the same if original tree is just a terminal"
    (is (= [0 8] (trees/random-subtree 8)))))

(deftest permute-branches-test
  (testing "should return tree with permuted arguments"
    (is (some? (or (= '(+ 1 2) (trees/permute-branches '(+ 1 2)))
                   (= '(+ 2 1) (trees/permute-branches '(+ 1 2))))))
    (is (some? (or (= '(+ (+ 2 3) 1) (trees/permute-branches '(+ 1 (+ 2 3))))
                   (= '(+ 1 (+ 3 2)) (trees/permute-branches '(+ 1 (+ 2 3)))))))
    (is (some? (or (= '(+ 1 (+ 2 3)) (trees/permute-branches '(+ 1 (+ 2 3))))
                   (= '(+ (+ 2 3) 1) (trees/permute-branches '(+ 1 (+ 2 3)))))))
    (is (some? (or (= '(+ (+ 1 2) (+ 3 4)) (trees/permute-branches '(+ (+ 1 2) (+ 3 4))))
                   (= '(+ (+ 3 4) (+ 1 2)) (trees/permute-branches '(+ (+ 1 2) (+ 3 4)))))))
    (is (some? (or (= '(+ 1 2 3) (trees/permute-branches '(+ 1 2 3)))
                   (= '(+ 1 3 2) (trees/permute-branches '(+ 1 2 3)))
                   (= '(+ 2 1 3) (trees/permute-branches '(+ 1 2 3)))
                   (= '(+ 2 3 1) (trees/permute-branches '(+ 1 2 3)))
                   (= '(+ 3 1 2) (trees/permute-branches '(+ 1 2 3)))
                   (= '(+ 3 2 1) (trees/permute-branches '(+ 1 2 3))))))))

(deftest sort-trees-by-adjusted-fitness-test
  (testing "should return a sorted-by-score array of trees with scores"
    (is (= {:population [{:tree "blah" :score 0.9}
                         {:tree "blah" :score 0.8}
                         {:tree "blah" :score 0.7}]}
           (trees/sort-trees-by-adjusted-fitness {:population [{:tree "blah" :score 0.7}
                                                               {:tree "blah" :score 0.8}
                                                               {:tree "blah" :score 0.9}]})))))


(deftest get-tree-cooresponding-to-score-test
  (testing "should return trees with scores lower than the rand val
  which means the probability of being chosen is proportional to the score"
    (bond/with-stub!
      [[rand (constantly 0.5)]]
      (is (= {:score 0.4 :tree '(+ 1 1)}
             (trees/get-tree-cooresponding-to-score
               {:population [{:score 0.4 :tree '(+ 1 1)}
                             {:score 0.6 :tree '(+ 1 1)}]}))))))

(deftest create-tree-by-mutation-test
  (testing "should mutate part of a given tree"
    (bond/with-stub!
      [[trees/get-tree-cooresponding-to-score (constantly {:tree '(+ 1 (+ 2 3))})]
       [trees/random-subtree (constantly [2 '(+ 2 3)])]
       [trees/create-tree (constantly '(+ 4 5 6 7))]]
      (let [original-state (setters/set-new-tree {} '(+ 1 (+ 2 3)))
            new-tree (trees/create-tree-by-mutation original-state)]
        (is (= '(+ 1 (+ 4 5 6 7)) new-tree))))))
