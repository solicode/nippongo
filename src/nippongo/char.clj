(ns nippongo.char
  (:require [nippongo.util :refer :all]))

(definline' hiragana? [c]
  `(let [code# (int ~c)]
     (or (<= 0x3041 code# 0x3094)
         (in? [0x309B 0x309C 0x30FC 0x30FD 0x30FE] code#))))

(definline' half-width-katakana? [c]
  `(<= 0xFF65 (int ~c) 0xFF9F))

(definline' full-width-katakana? [c]
  `(let [code# (int ~c)]
     (or (<= 0x30A1 code# 0x30FB) ; Katakana
         (<= 0x31F0 code# 0x31FF) ; Phonetic Extensions for Ainu
         (in? [0x309B 0x309C 0x30FC 0x30FD 0x30FE] code#)))) ; Punctuation and Symbols

(definline' katakana? [c]
  `(or (half-width-katakana? ~c)
       (full-width-katakana? ~c)))

(definline' kana? [c]
  `(let [code# (int ~c)]
     (or (<= 0x3041 code# 0x3094) ; Hiragana (without punctuation/symbols because it's included in the `katakana?` check)
         (katakana? ~c))))

(definline' kanji? [c]
  `(<= 0x4E00 (int ~c) 0x9FCC))

(definline' half-width-lowercase-letter? [c]
  `(<= (int \a) (int ~c) (int \z)))

(definline' half-width-uppercase-letter? [c]
  `(<= (int \A) (int ~c) (int \Z)))

(definline' full-width-lowercase-letter? [c]
  `(<= (int \ａ) (int ~c) (int \ｚ)))

(definline' full-width-uppercase-letter? [c]
  `(<= (int \Ａ) (int ~c) (int \Ｚ)))

(definline' half-width-letter? [c]
  `(or (half-width-lowercase-letter? ~c)
       (half-width-uppercase-letter? ~c)))

(definline' full-width-letter? [c]
  `(or (full-width-lowercase-letter? ~c)
       (full-width-uppercase-letter? ~c)))

(definline' letter? [c]
  `(or (half-width-letter? ~c)
       (full-width-letter? ~c)))

(definline' half-width-digit? [c]
  `(<= (int \0) (int ~c) (int \9)))

(definline' full-width-digit? [c]
  `(<= (int \０) (int ~c) (int \９)))

(definline' digit? [c]
  `(or (half-width-digit? ~c)
       (full-width-digit? ~c)))

(definline' romaji? [c]
  `(or (letter? ~c)
       (in? [\ā \ī \ū \ē \ō] ~c)))

(definline' ->dakuten [c]
  ; TODO: Consider using a map here if it's faster. Benchmarks needed.
  `(case ~c
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
     ~c))

(definline' ->handakuten [c]
  `(case ~c
     ; hiragana
     \は \ぱ, \ひ \ぴ, \ふ \ぷ, \へ \ぺ, \ほ \ぽ
     ; katakana
     \ハ \パ, \ヒ \ピ, \フ \プ, \ヘ \ペ, \ホ \ポ
     ~c))

(definline' full-width-hiragana->full-width-katakana [c]
  `(let [code# (int ~c)]
     (if (<= 0x3041 code# 0x3093)
       (char (+ code# 96))
       ~c)))

(definline' full-width-katakana->full-width-hiragana [c]
  `(let [code# (int ~c)]
     (cond
       (<= 0x30A1 code# 0x30F3) (char (- code# 96))
       (int \ヵ) \か
       (int \ヶ) \け
       (int \ヴ) \う
       :else ~c)))
