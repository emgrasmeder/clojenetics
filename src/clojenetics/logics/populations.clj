(ns clojenetics.logics.populations)

(defn population-allowance [state]
  (> (:population-size state) 0))
