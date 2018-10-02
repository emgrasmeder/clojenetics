(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.generations :as generations]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.utils :as utils]))

(defn set-defaults [state]
  (-> state
      (setters/set-functions utils/basic-functions)
      (setters/set-terminals utils/basic-terminals)
      (setters/set-numbers utils/basic-numbers)
      (setters/set-generations 1000)
      (setters/set-seed-count 1000)
      (setters/set-max-tree-depth 3)
      (setters/set-target 1234)
      (setters/set-min-or-max-for-obj-fn :minimize)
      (setters/set-objective-fn generations/raw-fitness-as-error)))

(defn do-genetic-programming [state]
  (log/debug "Planting trees... " state)
  (generations/do-many-generations (set-defaults state)))