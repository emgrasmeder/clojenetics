(ns clojenetics.logics.utils-test
  (:require [clojure.test :refer :all]
            [clojenetics.logics.utils :as utils]))

(deftest score-objective-fn-test
  (testing "should evaluate tree against objective function"
    (let [state {:target       100
                 :objective-fn (fn [t fn]
                                 (Math/abs (- t (eval fn))))}]
      (is (= 0 (utils/score-objective-fn state '(+ 100 0)))))
    (let [state {:target       200
                 :objective-fn (fn [t fn]
                                 (Math/abs (- t (eval fn))))}]
      (is (= 100 (utils/score-objective-fn state '(+ 100 0)))))))