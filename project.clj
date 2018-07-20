(defproject clojenetic "0.1.0-SNAPSHOT"
  :description "Clojure -> Genetic Programming => Clojenetic"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [circleci/bond "0.3.1"]]
  :main ^:skip-aot clojenetic.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
