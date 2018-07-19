(ns clojenetic.core
  (:require [com.stuartsierra.component :as component]))

;; I think the goal is to create a flow that works something like:

#_(-> (let-there-be-light)
    (set-terminals terminals)
    (set-functions functions)
    (heres-how-to-score-it minimize arbitrary-fn)
    (set-generation-limit limit))

(defrecord Base [terminals numbers functions]
  component/Lifecycle
  (start [component]
    (println "Initializing Base")
    (assoc component :base {:terminals terminals
                            :numbers numbers
                            :functions functions}))
  (stop [component]
    (assoc component :base nil)))

(defn base-init [terminals numbers functions]
  map->Base {:terminals terminals
             :numbers numbers
             :functions functions})

(defrecord MyComponent [arg1 arg2]
;; Implement the Lifecycle protocol
component/Lifecycle

(start [component]
(println "Initializing MyComponent")
(assoc component :state-after-init {:arg1 arg1
                                    :arg2 arg2
                                    :default-thing 12345}))

(stop [component]
(println ";; Stopping MyComponent")
(assoc component :state-after-init nil)))

;; Here's an optional constructor function
(defn new-mycomponent [arg1 arg2]
  (map->MyComponent {:arg1 "hello" :arg2 "world"}))

;; This is how we define behavior
(defn get-some-arg [my-component-instance some-arg]
  (printf "got argument: %s" some-arg)
  (get my-component-instance some-arg))


;; This is another component which can be a dependency of the other
(defrecord ExampleComponent [options cache my-component-instance scheduler]
  component/Lifecycle

  (start [this]
    (println ";; Starting ExampleComponent")
    (assoc this :admin (get-some-arg my-component-instance "admin")))

  (stop [this]
    (println ";; Stopping ExampleComponent")
    this))

(defn example-component [config-options]
  (map->ExampleComponent {:options config-options
                          :cache (atom {})}))


#_(defn example-system [config-options]
  (let [{:keys [host port]} config-options]
    (component/system-map
     :db (new-my-component-instance host port)
     :scheduler (new-scheduler)
     :app (component/using
           (example-component config-options)
           {:my-component-instance  :db
            :scheduler :scheduler}))))
