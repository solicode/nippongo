(ns nippongo.util)

(defmacro in? [coll val]
  (let [val-sym (gensym)]
    `(let [~val-sym ~val]
       (or ~@(for [x coll] `(= ~val-sym ~x))))))

(defmacro const
  "Evaluate an expression at compile-time."
  [body]
  (eval body))

(defmacro definline'
  "Like defmacro, except defines a named function whose body is the expansion,
  calls to which may be expanded inline as if it were a macro. Cannot be used
  with variadic (&) args."
  [name & decl]
  (let [[pre-args [args expr]] (split-with (comp not vector?) decl)
        [doc attr-map] (if (instance? String (first pre-args))
                         [(first pre-args) (fnext pre-args)]
                         [nil (first pre-args)])
        form (list `fn args expr)]
    `(defn ~name
       ~@(when (seq doc) [doc])
       ~(assoc attr-map :inline form)
       ~args
       ~(apply (eval form) args))))

(definline' map-string [f ^String s]
  `(when ~s
     (let [buffer# (.toCharArray ~s)]
       (dotimes [i# (.length ~s)]
         (aset buffer# i# (char (~f (aget buffer# i#)))))
       (String. buffer#))))

(definline' every-char? [pred ^String s]
  `(let [length# (.length ~s)]
     (loop [i# 0]
       (if (= i# length#)
         true
         (if (~pred (.charAt ~s i#))
           (recur (inc i#))
           false)))))

(definline' some-char? [pred ^String s]
  `(let [length# (.length ~s)]
     (loop [i# 0]
       (if (= i# length#)
         false
         (if (~pred (.charAt ~s i#))
           true
           (recur (inc i#)))))))
