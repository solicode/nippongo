(ns nippongo.core-test
  (:require [clojure.test :refer :all]
            [nippongo.core :refer :all]))

(deftest test-hiragana?
  (testing "hiragana? should be true"
    (are [x] (hiragana? x)
      \あ
      \か
      \さ
      "あかさたなはまやらわをん"))
  (testing "hiragana? should be false"
    (are [x] (not (hiragana? x))
      \a
      \z
      \1
      \7
      \ア
      \日
      \。
      \!)))

; TODO: Add tests for all other functions
