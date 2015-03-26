(defproject net.solicode/nippongo "0.1.0-SNAPSHOT"
  :description "A library with assorted functions for processing Japanese text."
  :url "https://github.com/solicode/nippongo"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :main nippongo.core
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[criterium "0.4.3"]]}})
