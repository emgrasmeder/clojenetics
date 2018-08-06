(ns clojenetics.logics.terminals
  (:require [clojure.tools.logging :as log]))

(defn rand-terminal
  [terminals numbers]
  (log/info "Choosing random terminal from " (concat terminals numbers))
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [max-tree-depth terminals numbers seeds-remaining]}]
  (if (or (zero? max-tree-depth)
          (< (rand) 0.5))
    (rand-terminal terminals numbers)
    false))