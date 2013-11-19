(defproject connect4 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/Apophenia/clojure-connect"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.match "0.2.0"]]
  :main ^:skip-aot connect4.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
