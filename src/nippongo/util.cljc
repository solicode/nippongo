(ns nippongo.util
  #?(:cljs
     (:import [goog.string StringBuffer]))
  #?(:cljs
     (:require-macros [nippongo.util :refer [in? char-code]])))

(defn char-code [c]
  #?(:clj  (int c)
     :cljs (.charCodeAt c 0)))

#?(:clj
   (defmacro in? [coll val]
     (let [val-sym (gensym)]
       `(let [~val-sym ~val]
          (or ~@(for [x coll] `(= ~val-sym ~x)))))))

#?(:clj
   (defn map-string [f ^String s]
     (when s
       (let [buffer (.toCharArray s)]
         (dotimes [i (.length s)]
           (aset buffer i (char (f (aget buffer i)))))
         (String. buffer))))

   :cljs
   (defn map-string [f s]
     (when s
       (let [buffer (StringBuffer.)]
         (dotimes [i (count s)]
           (.append buffer (f (aget s i))))
         (.toString buffer)))))

(defn every-char? [pred s]
  (let [length (count s)]
    (loop [i 0]
      (if (= i length)
        true
        (if (pred (.charAt s i))
          (recur (inc i))
          false)))))

(defn some-char? [pred s]
  (let [length (count s)]
    (loop [i 0]
      (if (= i length)
        false
        (if (pred (.charAt s i))
          true
          (recur (inc i)))))))
