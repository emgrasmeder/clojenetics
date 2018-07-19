(ns clojenetic.core-test
  (:require [clojure.test :refer :all]
            [clojenetic.core :as core]))

(deftest base-test
  (testing "constructs a component with terminals, numbers, and functions"
    (is (= {:terminals 1
            :numbers 2
            :functions 3} (core/base-init 1 2 3)))))
