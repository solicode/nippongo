(ns nippongo.test.core
  (:require [nippongo.core :as n]
    #?(:cljs [cljs.test :as t :refer-macros [deftest is are testing]]
       :clj [clojure.test :as t :refer [deftest is are testing]])))

(deftest test-hiragana?
  (testing "hiragana? should be true"
    (are [x] (n/hiragana? x)
      \あ
      \か
      \さ
      "あかさたなはまやらわをん"))
  (testing "hiragana? should be false"
    (are [x] (not (n/hiragana? x))
      \a
      \z
      \1
      \7
      \ア
      \日
      \。
      \!)))

; TODO: Add tests for all other functions
