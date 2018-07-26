(ns clojenetics.logics.setters
  (:require [clojure.tools.logging :as log]))

(defn set-terminals [state terminals]
  (log/info "Setting terminals: " terminals)
  (assoc state :terminals terminals))

(defn set-numbers [state numbers]
  (log/info "Setting numbers: " numbers)
  (assoc state :numbers numbers))

(defn set-functions [state functions]
  (log/info "Setting functions: " functions)
  (assoc state :functions functions))

(defn set-objective-fn [state fn]
  (log/info "Setting objective-fn: " fn)
  (assoc state :objective-fn fn))

(defn set-tree-depth [state limit]
  (log/info "Setting tree-depth: " limit)
  (assoc state :tree-depth limit))

(defn set-population-size [state population]
  (log/info "Setting population-size: " population)
  (assoc state :population-size population))

(defn set-target [state target]
  (log/info "Setting target: " target)
  (assoc state :target target))

(defn set-best-tree [state tree-score]
  (log/info "Setting best tree: " tree-score)
  (assoc state :best-tree tree-score))
