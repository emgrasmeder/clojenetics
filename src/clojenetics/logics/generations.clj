(ns clojenetics.logics.generations
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.utils :as utils]
            [clojenetics.logics.setters :as setters]
            [clojenetics.logics.trees :as trees]))

(defn raw-fitness-as-error [computed target]
  computed)

(defn standardized-fitness
  ([raw-fitness] (standardized-fitness raw-fitness 0))
  ([raw-fitness max-raw-fitness]
   (utils/abs (- max-raw-fitness raw-fitness))))

(defn adjusted-fitness [standardized-fitness]
  (/ 1 (+ 1 standardized-fitness)))

(declare generate-trees)

(defn do-generation [state]
  (log/infof "%s trees left to generate in this generation" (:seeds-remaining state))
  (if (utils/strictly-positive? (:seeds-remaining state))
    (let [tree (trees/create-tree state)
          state (setters/dec-seeds-remaining state)
          state (setters/set-new-tree state tree)]
      (do-generation state))
    (setters/set-scores state)))

(defn do-many-generations [state]
  (log/infof "%s generations left to make" (:generations-remaining state))
  (if (utils/strictly-positive? (:generations-remaining state))
    (let [population (:trees (do-generation state))
          state (setters/dec-generations state)
          state (setters/set-population state population)]
      (do-many-generations state))
    state))

;; 0. Check if is initial generation (by detecting if no trees already exist)
;; 1. Generate trees (either randomly (if first generation) or by propagation-technique)
;; 2. Get scores
;; 3. Do next generation
