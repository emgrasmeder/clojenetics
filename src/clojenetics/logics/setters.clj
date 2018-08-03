(ns clojenetics.logics.setters
  (:require [clojure.tools.logging :as log]
            [clojenetics.logics.utils :as utils]))

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

(defn set-seed-count [state seeds]
  (log/info "Setting:seeds-remaining: " seeds)
  (assoc state :seeds-remaining seeds))

(defn set-target [state target]
  (log/info "Setting target: " target)
  (assoc state :target target))

(defn set-best-tree [state]
  (log/info "Setting best tree")
  (let [f (if (= :maximize (:min-or-max-objective-fn state)) > <)
        trees (:trees state)
        best-tree (if (= 1 (count trees))
                    (first trees)
                    (reduce (fn [a b]
                              (if (f (:score a) (:score b)) b a))
                            trees))]
    (assoc state :best-tree best-tree)))

(defn set-new-tree [state tree]
  (log/info "Setting new tree: " tree)
  (assoc state :trees (concat (:trees state) [{:tree tree}])))

(defn set-scores [{:keys [trees objective-fn target] :as state}]
  (log/info "Setting scores for: " trees)
  (let [new-trees (map (fn [tree-hash]
                         (assoc tree-hash :score
                                          (utils/score-objective-fn state (:tree tree-hash)))) trees)]
    (assoc state :trees new-trees)))

(defn dec-seeds-remaining [state]
  (assoc state :seeds-remaining (dec (:seeds-remaining state))))