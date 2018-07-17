(ns clojentic.robot
  (:require [fungp.core :as fungp]
            [fungp.util :as utils]
            [clojure.pprint :as pp]))

(def MAX_T "Maximum number of ticks" 250)
(def PENALTY "Points added when time is up" 250)
(def COIN_COUNT "Number of coins on grid" 5)
(def GRID_SIZE "Length of one side of the grid" 5)
(def CRITERIA "Acceptable minimum value" 200)
(def TRIALS "Number of trials for each program" 4)

(def ^:dynamic robot-x)
(def ^:dynamic robot-y)
(def ^:dynamic robot-dir)
(def ^:dynamic ticks)
(def ^:dynamic coins-available)


(defn generate-coins-list
  "Generate a list of coins consisting of [x y] pairs."
  []
  (take COIN_COUNT (shuffle (for [x (range GRID_SIZE)
                                  y (range GRID_SIZE)]
                              [x y]))))

(defn new-x
  "Return a new x coordinate given a direction for movement."
  [dir x]
  (mod
    (case dir
      0 (dec x),
      1 x,
      2 (inc x),
      3 x)
    GRID_SIZE))

(defn new-y
  "Return a new y coordinate given a direction for movement."
  [dir y]
  (mod
    (case dir
      0 y,
      1 (inc y),
      2 y,
      3 (dec y))
    GRID_SIZE))

(defn coin-at?
  "Check if a coin exists at a certain location on the grid.
   Expects x and y coordinates and the coins sequence."
  [x y coins] (some #(= % [x y]) coins))

(defn coin-ahead?
  "Robot sensor to detect if a coin is in front of the robot."
  [] (coin-at? (new-x robot-dir robot-x)
               (new-y robot-dir robot-y)
               coins-available))

(defmacro detect-coin
  "Use robot's sensors to look for a coin, and if it's found execute
   the first branch, otherwise execute the second branch."
  [found not-found]
  `(if (coin-ahead?)
     ~found
     ~not-found))

(defn left
  "Make the robot turn left."
  []
  (set! robot-dir (mod (- robot-dir 1) 4))
  (set! ticks (inc ticks)))

(defn right
  "Make the robot turn right."
  []
  (set! robot-dir (mod (+ robot-dir 1) 4))
  (set! ticks (inc ticks)))

(defn move-forward
  "Make the robot move forward one step in whatever direction it is facing."
  []
  (set! robot-x (new-x robot-dir robot-x))
  (set! robot-y (new-y robot-dir robot-y))
  (set! ticks (inc ticks))
  (when (coin-at? robot-x robot-y coins-available)
    (set! coins-available (remove #(= % [robot-x robot-y]) coins-available))))


(def robot-terminals '[(left) (right) (move-forward)])
(def robot-functions '[[detect-coin 2] [do 2] [do 3]])

(defn simulate-robot
  "This function repeatedly runs the evolved code and checks its side effects
   (the dynamic variables) to see if the robot has run out of time or collected
   all of the coins."
  [f]
  (binding [robot-x (int (/ GRID_SIZE 2))
            robot-y (int (/ GRID_SIZE 2))
            robot-dir 0
            ticks 0
            coins-available (generate-coins-list)]
    (loop []
      (f)
      (cond (> ticks MAX_T) (+ PENALTY ticks (count coins-available))
            (empty? coins-available) ticks
            :else (recur)))))

(defn robot-error
  "Calculate the error of the evolved program."
  [tree]
  (let [f (eval (list 'fn [] tree))]
    (apply + (repeatedly TRIALS #(simulate-robot f)))))

(defn robot-fitness
  "Calculate the fitness, taking the criteria into account."
  [tree]
  (let [error (robot-error tree)]
    (if (> error CRITERIA) error 0)))

(defn robot-report
  [tree fitness]
  (pp/pprint (nth tree 2))
  (println (str "Error:\t" fitness "\n\n")))

(defn test-robots [n1 n2]
  (println "Robot Grid Movement Control Problem")
  (let [options {:max-depth 4
                 :terminals robot-terminals
                 :functions robot-functions
                 :fitness robot-fitness
                 :report robot-report}
        [tree score] (rest (fungp/run-genetic-programming options))]
    (robot-report tree score)))
