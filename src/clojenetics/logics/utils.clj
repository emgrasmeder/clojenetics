(ns clojenetics.logics.utils
  (:require [clojure.tools.logging :as log]))

(defn score-objective-fn [{:keys [target objective-fn] :as state} tree]
  (log/debugf "Scoring objective fn with state %s and tree %s" state tree)
  (objective-fn target tree))

(defn apply-min-or-max [{:keys [min-or-max-objective-fn]}]
  (if (= min-or-max-objective-fn :minimize)
    <
    >))
