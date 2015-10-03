(ns nippongo.test.runner
  (:require [cljs.test :as t]))

(enable-console-print!)

(defn -main [& args]
  (t/run-all-tests #"nippongo\.test\..+"))

(set! *main-cli-fn* -main)
