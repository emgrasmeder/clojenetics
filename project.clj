(defproject clojenetics "0.1.0-SNAPSHOT"
  :description "Clojure -> Genetic Programming => clojenetics"
  :url "https://github.com/emilyagras/clojenetics"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [circleci/bond "0.3.1"]
                 [com.taoensso/timbre "4.10.0"]]
  :main ^:skip-aot clojenetics.core
  :target-path "target/%s"
  :profiles {:dev     {:env {:timbre-level :info}}
             :uberjar {:aot :all}})
