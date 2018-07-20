(ns clojenetic.core)

(defn set-terminals [state terminals]
  (assoc state :terminals terminals))

(defn set-functions [state functions]
  (assoc state :functions functions))

(def target-number
  100)

(defn objective-fn [proposed-solution-fn]
  (Math/abs (- target-number (eval proposed-solution-fn))))

(defn set-objective-fn [state fn]
  (assoc state :objective-fn fn))


(defn set-generation-limit [state limit]
  (assoc state :generation-limit limit))

(def functions
  [(let [] ['+ 2])
   (let [] ['- 2])])

(def terminals
  '[])

(def numbers
  [1 2 3 4 5 6 7 8 9 0])

(def generation-limit
  10)

(defn get-rand-terminal
  [terminals numbers]
  (rand-nth (concat terminals numbers)))

(defn create-tree
  [{:keys [generation-limit terminals numbers functions]}]
  (if (or  (zero? generation-limit)
           (< (rand) 0.5) (get-rand-terminal terminals numbers))
    (let [[func arity] (rand-nth functions)]
      (cons func (repeatedly arity
                             #(create-tree (dec generation-limit)
                                           terminals
                                           numbers
                                           functions))))))

(defn solve-objective-fn [state min-or-max]
  (let [proposed-solution (create-tree state)]))



(-> {}
    (set-terminals terminals)
    (set-functions functions)
    (set-objective-fn objective-fn)
    (set-generation-limit generation-limit)
    (solve-objective-fn :minimize))
