(ns integration.number-guess-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as clojenetics]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.generations :as generations]
            [bond.james :as bond]))

(deftest number-guess-test
  (testing "should call functions as many times as expected"
    (let [num-generations 1
          state (-> {}
                    (setters/set-functions utils/basic-functions)
                    (setters/set-terminals utils/basic-terminals)
                    (setters/set-numbers utils/basic-numbers)
                    (setters/set-generations num-generations)
                    (setters/set-seed-count 1)
                    (setters/set-max-tree-depth 1)
                    (setters/set-target 1234)
                    (setters/set-min-or-max-for-obj-fn :minimize)
                    (setters/set-objective-fn generations/raw-fitness-as-error))]
      (bond/with-spy
        [generations/do-many-generations
         setters/set-best-tree]
        (clojenetics/do-genetic-programming state)
        (is (= (+ 1 num-generations)
               (-> generations/do-many-generations bond/calls count))
            "do-many-generations should be called num-generations + 1 call that calculates score")
        (is (= 1 (-> setters/set-best-tree bond/calls count)))))))