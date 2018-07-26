(ns clojenetics.logics.terminals
  (:require [clojure.tools.logging :as log]))

(defn rand-terminal
  [terminals numbers]
  (log/info "Choosing random terminal")
  (rand-nth (concat terminals numbers)))

(defn try-for-terminal [{:keys [tree-depth terminals numbers population-size]}]
  (if (or (zero? tree-depth)
          (zero? population-size)
          (< (rand) 0.5))
    (rand-terminal terminals numbers)
    false))