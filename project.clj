(defproject http-async "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.3.442"]

                 ;; server
                 [ring "1.7.0-RC1"]
                 [ring/ring-defaults "0.3.2"]


                 ;; routing
                 [compojure "1.6.1"]

                 ;; async server
                 [aleph "0.4.6"]

                 ;; async lib
                 [manifold "0.1.8"]

                 ;;json
                 [cheshire "5.8.0"]

                 ]
  :main ^:skip-aot http-async.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
