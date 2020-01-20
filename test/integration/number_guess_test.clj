(ns integration.number-guess-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as clojenetics]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.generations :as generations]
            [bond.james :as bond]))

(deftest number-guess-with-permutations-test
  (testing "should call functions as many times as expected when doing permutations"
    (bond/with-spy
      [generations/do-many-generations
       utils/score-objective-fn
       setters/set-scores
       setters/set-best-tree]
      (let [num-generations 10
            num-seeds 10
            state (-> {}
                      (setters/set-functions utils/math-operations)
                      (setters/set-terminals utils/basic-terminals)
                      (setters/set-numbers utils/basic-numbers)
                      (setters/set-generations num-generations)
                      (setters/set-seed-count num-seeds)
                      (setters/set-max-tree-depth 4)
                      (setters/set-target 123)
                      (setters/set-propagation-technique :permutation)
                      (setters/set-min-or-max-for-obj-fn :minimize)
                      (setters/set-objective-fn generations/raw-fitness-as-error))
            new-state (clojenetics/do-genetic-programming state)]
        (is (= 1 (-> setters/set-best-tree bond/calls count)))
        (is (= num-generations (-> setters/set-scores bond/calls count)))
        (is (= (* num-generations num-seeds) (-> utils/score-objective-fn bond/calls count)))
        (is (:best-tree new-state))
        (is (= num-seeds (count (:population new-state))))))))

(deftest number-guess-with-mutations-test
  (testing "should call functions as many times as expected when doing mutations"
    (bond/with-spy
      [utils/score-objective-fn
       setters/set-scores
       setters/set-best-tree]
      (let [num-generations 10
            num-seeds 10
            state (-> {}
                      (setters/set-functions utils/math-operations)
                      (setters/set-terminals utils/basic-terminals)
                      (setters/set-numbers utils/basic-numbers)
                      (setters/set-generations num-generations)
                      (setters/set-seed-count num-seeds)
                      (setters/set-max-tree-depth 4)
                      (setters/set-target 123)
                      (setters/set-propagation-technique :mutation)
                      (setters/set-min-or-max-for-obj-fn :minimize)
                      (setters/set-objective-fn generations/raw-fitness-as-error))
            new-state (clojenetics/do-genetic-programming state)]
        (is (= 1 (-> setters/set-best-tree bond/calls count)))
        (is (= num-generations (-> setters/set-scores bond/calls count)))
        (is (= (* num-generations num-seeds) (-> utils/score-objective-fn bond/calls count)))
        (is (:best-tree new-state))
        (is (= num-seeds (count (:population new-state))))))))

(deftest number-guess-with-crossover-test
  (testing "should call functions as many times as expected when doing crossover"
    (bond/with-spy
      [setters/set-best-tree
       setters/set-scores
       utils/score-objective-fn]
      (let [num-generations 10
            num-seeds 10
            state (-> {}
                      (setters/set-functions utils/math-operations)
                      (setters/set-terminals utils/basic-terminals)
                      (setters/set-numbers utils/basic-numbers)
                      (setters/set-generations num-generations)
                      (setters/set-seed-count num-seeds)
                      (setters/set-max-tree-depth 4)
                      (setters/set-target 123)
                      (setters/set-propagation-technique :crossover)
                      (setters/set-min-or-max-for-obj-fn :minimize)
                      (setters/set-objective-fn generations/raw-fitness-as-error))
            new-state (clojenetics/do-genetic-programming state)]
        (is (= 1 (-> setters/set-best-tree bond/calls count)))
        (is (= num-generations (-> setters/set-scores bond/calls count)))
        (is (= (* num-generations num-seeds) (-> utils/score-objective-fn bond/calls count)))
        (is (:best-tree new-state))
        (is (= num-seeds (count (:population new-state))))))))