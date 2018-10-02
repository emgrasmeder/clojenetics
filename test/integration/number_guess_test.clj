(ns integration.number-guess-test
  (:require [clojure.test :refer :all]
            [clojenetics.core :as clojenetics]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.trees :as trees]
            [clojenetics.logics.setters :as setters]
            [clojure.tools.logging :as log]))

#_(deftest number-guess-test
  (testing "should do something"
    (is (= 1234 (clojenetics/do-genetic-programming {})))))