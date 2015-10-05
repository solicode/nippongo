(defproject net.solicode/nippongo "0.1.0-SNAPSHOT"
  :description "A library with assorted functions for processing Japanese text."
  :url "https://github.com/solicode/nippongo"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :main nippongo.core
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]]
  :plugins [[lein-cljsbuild "1.1.0"]]
  :cljsbuild {:builds        [{:id           "test"
                               :source-paths ["src" "test"]
                               :compiler     {:output-to     "target/cljs/test-runner.js"
                                              :output-dir    "target/cljs"
                                              :target        :nodejs
                                              :optimizations :simple}}]
              :test-commands {"test" ["node" "target/cljs/test-runner.js"]}}
  :profiles {:dev {:jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
                   :dependencies [[criterium "0.4.3"]]}})
