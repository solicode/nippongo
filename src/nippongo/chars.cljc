(ns nippongo.chars
  #?(:clj (:require [nippongo.util :refer :all]))
  #?(:cljs (:require [nippongo.util :refer [char-code] :refer-macros [in?]])))

(defn hiragana? [c]
  (let [code (char-code c)]
    (or (<= 0x3041 code 0x3094)
        (in? [0x309B 0x309C 0x30FC 0x30FD 0x30FE] code))))

(defn half-width-katakana? [c]
  (<= 0xFF65 (char-code c) 0xFF9F))

(defn full-width-katakana? [c]
  (let [code (char-code c)]
    (or (<= 0x30A1 code 0x30FB) ; Katakana
        (<= 0x31F0 code 0x31FF) ; Phonetic Extensions for Ainu
        (in? [0x309B 0x309C 0x30FC 0x30FD 0x30FE] code)))) ; Punctuation and Symbols

(defn katakana? [c]
  (or (half-width-katakana? c)
      (full-width-katakana? c)))

(defn kana? [c]
  (let [code (char-code c)]
    (or (<= 0x3041 code 0x3094) ; Hiragana (without punctuation/symbols because it's included in the katakana? check)
        (katakana? c))))

(defn kanji? [c]
  (<= 0x4E00 (char-code c) 0x9FCC))

(defn half-width-lowercase-letter? [c]
  (<= (char-code \a) (char-code c) (char-code \z)))

(defn half-width-uppercase-letter? [c]
  (<= (char-code \A) (char-code c) (char-code \Z)))

(defn full-width-lowercase-letter? [c]
  (<= (char-code \ａ) (char-code c) (char-code \ｚ)))

(defn full-width-uppercase-letter? [c]
  (<= (char-code \Ａ) (char-code c) (char-code \Ｚ)))

(defn half-width-letter? [c]
  (or (half-width-lowercase-letter? c)
      (half-width-uppercase-letter? c)))

(defn full-width-letter? [c]
  (or (full-width-lowercase-letter? c)
      (full-width-uppercase-letter? c)))

(defn letter? [c]
  (or (half-width-letter? c)
      (full-width-letter? c)))

(defn half-width-digit? [c]
  (<= (char-code \0) (char-code c) (char-code \9)))

(defn full-width-digit? [c]
  (<= (char-code \０) (char-code c) (char-code \９)))

(defn digit? [c]
  (or (half-width-digit? c)
      (full-width-digit? c)))

(defn romaji? [c]
  (or (letter? c)
      (in? [\ā \ī \ū \ē \ō] c)))

(defn ->dakuten [c]
  ; TODO: Consider using a map here if it's faster. Benchmarks needed.
  (case c
    ; hiragana
    \か \が, \き \ぎ, \く \ぐ, \け \げ, \こ \ご
    \さ \ざ, \し \じ, \す \ず, \せ \ぜ, \そ \ぞ
    \た \だ, \ち \ぢ, \つ \づ, \て \で, \と \ど
    \は \ば, \ひ \び, \ふ \ぶ, \へ \べ, \ほ \ぼ
    ; katakana
    \カ \ガ, \キ \ギ, \ク \グ, \ケ \ゲ, \コ \ゴ
    \サ \ザ, \シ \ジ, \ス \ズ, \セ \ゼ, \ソ \ゾ
    \タ \ダ, \チ \ヂ, \ツ \ヅ, \テ \デ, \ト \ド
    \ハ \バ, \ヒ \ビ, \フ \ブ, \ヘ \ベ, \ホ \ボ
    c))

(defn ->handakuten [c]
  (case c
    ; hiragana
    \は \ぱ, \ひ \ぴ, \ふ \ぷ, \へ \ぺ, \ほ \ぽ
    ; katakana
    \ハ \パ, \ヒ \ピ, \フ \プ, \ヘ \ペ, \ホ \ポ
    c))

(defn full-width-hiragana->full-width-katakana [c]
  (let [code (char-code c)]
    (if (<= 0x3041 code 0x3093)
      (char (+ code 96))
      c)))

(defn full-width-katakana->full-width-hiragana [c]
  (let [code (int c)]
    (cond
      (<= 0x30A1 code 0x30F3) (char (- code 96))
      (char-code \ヵ) \か
      (char-code \ヶ) \け
      (char-code \ヴ) \う
      :else c)))
