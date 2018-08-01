(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.trees :as trees]))

(defn grow [state]
  (log/info "Growing a good tree... " state)
  (trees/grow-trees state))