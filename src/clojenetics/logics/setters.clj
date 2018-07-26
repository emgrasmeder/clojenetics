(ns clojenetics.logics.setters
  (:require [clojure.tools.logging :as log]))

(defn set-terminals [state terminals]
  (log/debug "Setting terminals: " terminals)
  (assoc state :terminals terminals))

(defn set-numbers [state numbers]
  (log/debug "Setting numbers: " numbers)
  (assoc state :numbers numbers))

(defn set-functions [state functions]
  (log/debug "Setting functions: " functions)
  (assoc state :functions functions))

(defn set-objective-fn [state fn]
  (log/debug "Setting objective-fn: " fn)
  (assoc state :objective-fn fn))

(defn set-tree-depth [state limit]
  (log/debug "Setting tree-depth: " limit)
  (assoc state :tree-depth limit))

(defn set-population-size [state population]
  (log/debug "Setting population-size: " population)
  (assoc state :population-size population))

(defn set-target [state target]
  (log/debug "Setting target: " target)
  (assoc state :target target))
