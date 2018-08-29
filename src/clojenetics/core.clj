(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.trees :as trees]))

(defn grow [state]
  (log/info "Planting trees... " state)
  (trees/do-generation state))