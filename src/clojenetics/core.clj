(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.trees :as trees]))

(defn grow [state]
  (log/info "Doing something... " state)
  (first (:best-tree (trees/get-best-tree state))))