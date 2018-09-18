(ns clojenetics.core
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.generations :as generations]))

(defn grow [state]
  (log/info "Planting trees... " state)
  (generations/do-generation state))