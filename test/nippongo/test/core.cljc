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

(deftest test-romaji->hiragana
  (testing "romaji should convert to hiragana"
    (are [x expected] (= (n/romaji->hiragana x) expected)
      "shinjuku" "しんじゅく"
      "sinjuku" "しんじゅく"
      "fuujin" "ふうじん"
      "raijin" "らいじん"
      "nihongo" "にほんご"))
  (testing "should not convert capital letters, numbers, symbols, non-romaji, etc."
    (are [x expected] (= (n/romaji->hiragana x) expected)
      "MEGURO" "MEGURO"
      "GOOD JOB desu" "GOOD JOB です"
      "iikuni (1192) tsukurou kamakurabakufu" "いいくに (1192) つくろう かまくらばくふ")))

; TODO: Add tests for all other functions
