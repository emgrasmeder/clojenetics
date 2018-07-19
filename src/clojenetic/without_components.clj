(ns clojenetic.without-components)

(defn let-there-be-light []
  {})

(defn set-terminals [state terminals]
  (assoc state :terminals terminals))

(defn set-functions [state functions]
  (assoc state :functions functions))

(def target-number
  100)

(defn find-the-number [proposed-solution-fn]
  (Math/abs (- target-number (eval proposed-solution-fn))))

(defn objective-function [state min-or-max fn]
  (assoc state :objective-fn fn)
  (assoc state :min-or-max min-or-max))

(defn set-generation-limit [state limit]
  (assoc state :generation-limit limit))

(defn do-the-thing [state]
  )

(-> (let-there-be-light)
    (set-terminals terminals)
    (set-functions functions)
    (objective-function minimize arbitrary-fn)
    (set-generation-limit limit)
    (do-the-thing))

(def functions
  [(let [] ['+ 2])
   (let [] ['- 2])])

(def terminals
  '[])

(def numbers
  [1 2 3 4 5 6 7 8 9 0])

(def mutation-depth
  10)

(defn get-rand-terminal
  [terminals numbers]
  (rand-nth (concat terminals numbers)))

(defn create-tree
  [mutation-depth terminals numbers functions]
  (cond (zero? mutation-depth) (get-rand-terminal terminals numbers)
        (< (rand) 0.5) (get-rand-terminal terminals numbers)
        :else (let [[func arity] (rand-nth functions)]
                (cons func (repeatedly arity
                                       #(create-tree (dec mutation-depth)
                                                     terminals
                                                     numbers
                                                     functions))))))
