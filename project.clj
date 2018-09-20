(defproject clojenetics "0.1.0-SNAPSHOT"
  :description "Clojure -> Genetic Programming => clojenetics"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [circleci/bond "0.3.1"]
                 [org.clojure/tools.logging "0.4.1"]]
  :main ^:skip-aot clojenetics.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
