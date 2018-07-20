(ns clojenetics.number-guess
  (:require [clojenetics.core :refer :all]))

(def functions
  [(let [] ['+ 2])
   (let [] ['- 2])])

(def terminals
  '[])

(def numbers
  [1 2 3 4 5 6 7 8 9 0])

(def generation-limit
  10)

(def target-number
  100)

(defn objective-fn [proposed-solution-fn]
  (println "Scoring tree: " proposed-solution-fn)
  (Math/abs (- target-number (eval proposed-solution-fn))))

(defn do-the-thing [state]
  (-> state
      (set-terminals terminals)
      (set-numbers numbers)
      (set-functions functions)
      (set-objective-fn objective-fn)
      (set-generation-limit generation-limit)
      (solve-objective-fn :minimize)))
