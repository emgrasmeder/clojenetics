(ns clojenetics.core
  (:require [taoensso.timbre :refer [debug debugf]]
            [clojenetics.logics.generations :as generations]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.utils :as utils]))

(defn do-genetic-programming [state]
  (debug "Planting trees... " state)
  (generations/do-many-generations state))