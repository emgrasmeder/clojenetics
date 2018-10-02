(ns clojenetics.logics.terminals
  (:require [clojure.tools.logging :as log]))

(defn rand-terminal
  [terminals numbers]
  (log/debug "Choosing random terminal from " (concat terminals numbers))
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [current-tree-depth terminals numbers seeds-remaining max-tree-depth]}]
  (log/debug "Trying for either false or a terminal out of" terminals numbers)
  (cond
    (= current-tree-depth max-tree-depth) false
    (< current-tree-depth 1) (rand-terminal terminals numbers)
    (< (rand) 0.5) false
    :else (rand-terminal terminals numbers)))