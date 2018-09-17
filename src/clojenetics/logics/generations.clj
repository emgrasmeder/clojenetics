(ns clojenetics.logics.generations
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.utils :as utils]))

(defn raw-fitness-as-error [computed target]
  computed)

(defn standardized-fitness
  ([raw-fitness] (standardized-fitness raw-fitness 0))
  ([raw-fitness max-raw-fitness]
   (utils/abs (- max-raw-fitness raw-fitness))))

(defn adjusted-fitness [standardized-fitness]
  (/ 1 (+ 1 standardized-fitness)))

