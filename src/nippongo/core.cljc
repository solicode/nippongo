(ns nippongo.core
  (:require [nippongo.chars :as ch]
            [nippongo.util :refer [map-string every-char? some-char?]])
  #?(:cljs (:import [goog.string StringBuffer])))

(def verb-endings (map str "ぶぐくむるすつうぬ"))

(def romaji-table
  {"a"    "あ"
   "ba"   "ば"
   "be"   "べ"
   "bi"   "び"
   "bo"   "ぼ"
   "bu"   "ぶ"
   "bya"  "びゃ"
   "bye"  "びぇ"
   "byi"  "びぃ"
   "byo"  "びょ"
   "byu"  "びゅ"
   "ca"   "か"
   "ce"   "せ"
   "cha"  "ちゃ"
   "che"  "ちぇ"
   "chi"  "ち"
   "cho"  "ちょ"
   "chu"  "ちゅ"
   "ci"   "し"
   "co"   "こ"
   "cu"   "く"
   "cya"  "ちゃ"
   "cye"  "ちぇ"
   "cyi"  "ちぃ"
   "cyo"  "ちょ"
   "cyu"  "ちゅ"
   "d'i"  "でぃ"
   "d'u"  "どぅ"
   "d'yu" "でゅ"
   "da"   "だ"
   "de"   "で"
   "dha"  "でゃ"
   "dhe"  "でぇ"
   "dhi"  "でぃ"
   "dho"  "でょ"
   "dhu"  "でゅ"
   "di"   "ぢ"
   "do"   "ど"
   "du"   "づ"
   "dwa"  "どぁ"
   "dwe"  "どぇ"
   "dwi"  "どぃ"
   "dwo"  "どぉ"
   "dwu"  "どぅ"
   "dya"  "ぢゃ"
   "dye"  "ぢぇ"
   "dyi"  "ぢぃ"
   "dyo"  "ぢょ"
   "dyu"  "ぢゅ"
   "e"    "え"
   "fa"   "ふぁ"
   "fe"   "ふぇ"
   "fi"   "ふぃ"
   "fo"   "ふぉ"
   "fu"   "ふ"
   "fya"  "ふゃ"
   "fyo"  "ふょ"
   "fyu"  "ふゅ"
   "ga"   "が"
   "ge"   "げ"
   "gi"   "ぎ"
   "go"   "ご"
   "gu"   "ぐ"
   "gwa"  "ぐぁ"
   "gwe"  "ぐぇ"
   "gwi"  "ぐぃ"
   "gwo"  "ぐぉ"
   "gwu"  "ぐぅ"
   "gya"  "ぎゃ"
   "gye"  "ぎぇ"
   "gyi"  "ぎぃ"
   "gyo"  "ぎょ"
   "gyu"  "ぎゅ"
   "ha"   "は"
   "he"   "へ"
   "hi"   "ひ"
   "ho"   "ほ"
   "hu"   "ふ"
   "hwa"  "ふぁ"
   "hwe"  "ふぇ"
   "hwi"  "ふぃ"
   "hwo"  "ふぉ"
   "hwyu" "ふゅ"
   "hya"  "ひゃ"
   "hye"  "ひぇ"
   "hyi"  "ひぃ"
   "hyo"  "ひょ"
   "hyu"  "ひゅ"
   "i"    "い"
   "ja"   "じゃ"
   "je"   "じぇ"
   "ji"   "じ"
   "jo"   "じょ"
   "ju"   "じゅ"
   "jya"  "じゃ"
   "jye"  "じぇ"
   "jyi"  "じぃ"
   "jyo"  "じょ"
   "jyu"  "じゅ"
   "ka"   "か"
   "ke"   "け"
   "ki"   "き"
   "ko"   "こ"
   "ku"   "く"
   "kwa"  "くぁ"
   "kwe"  "くぇ"
   "kwi"  "くぃ"
   "kwo"  "くぉ"
   "kwu"  "くぅ"
   "kya"  "きゃ"
   "kye"  "きぇ"
   "kyi"  "きぃ"
   "kyo"  "きょ"
   "kyu"  "きゅ"
   "la"   "ぁ"
   "le"   "ぇ"
   "li"   "ぃ"
   "lka"  "ヵ"
   "lke"  "ヶ"
   "lo"   "ぉ"
   "ltsu" "っ"
   "ltu"  "っ"
   "lu"   "ぅ"
   "lwa"  "ゎ"
   "lya"  "ゃ"
   "lye"  "ぇ"
   "lyi"  "ぃ"
   "lyo"  "ょ"
   "lyu"  "ゅ"
   "ma"   "ま"
   "me"   "め"
   "mi"   "み"
   "mo"   "も"
   "mu"   "む"
   "mya"  "みゃ"
   "mye"  "みぇ"
   "myi"  "みぃ"
   "myo"  "みょ"
   "myu"  "みゅ"
   "n"    "ん"
   "n'"   "ん"
   "na"   "な"
   "ne"   "ね"
   "ni"   "に"
   "nn"   "ん"
   "no"   "の"
   "nu"   "ぬ"
   "nya"  "にゃ"
   "nye"  "にぇ"
   "nyi"  "にぃ"
   "nyo"  "にょ"
   "nyu"  "にゅ"
   "o"    "お"
   "pa"   "ぱ"
   "pe"   "ぺ"
   "pi"   "ぴ"
   "po"   "ぽ"
   "pu"   "ぷ"
   "pya"  "ぴゃ"
   "pye"  "ぴぇ"
   "pyi"  "ぴぃ"
   "pyo"  "ぴょ"
   "pyu"  "ぴゅ"
   "qa"   "くぁ"
   "qe"   "くぇ"
   "qi"   "くぃ"
   "qo"   "くぉ"
   "qu"   "く"
   "ra"   "ら"
   "re"   "れ"
   "ri"   "り"
   "ro"   "ろ"
   "ru"   "る"
   "rya"  "りゃ"
   "rye"  "りぇ"
   "ryi"  "りぃ"
   "ryo"  "りょ"
   "ryu"  "りゅ"
   "sa"   "さ"
   "se"   "せ"
   "sha"  "しゃ"
   "she"  "しぇ"
   "shi"  "し"
   "sho"  "しょ"
   "shu"  "しゅ"
   "si"   "し"
   "so"   "そ"
   "su"   "す"
   "sya"  "しゃ"
   "sye"  "しぇ"
   "syi"  "しぃ"
   "syo"  "しょ"
   "syu"  "しゅ"
   "t'i"  "てぃ"
   "t'u"  "とぅ"
   "t'yu" "てゅ"
   "ta"   "た"
   "te"   "て"
   "tha"  "てゃ"
   "the"  "てぇ"
   "thi"  "てぃ"
   "tho"  "てょ"
   "thu"  "てゅ"
   "ti"   "ち"
   "to"   "と"
   "tsa"  "つぁ"
   "tse"  "つぇ"
   "tsi"  "つぃ"
   "tso"  "つぉ"
   "tsu"  "つ"
   "tu"   "つ"
   "twa"  "とぁ"
   "twe"  "とぇ"
   "twi"  "とぃ"
   "two"  "とぉ"
   "twu"  "とぅ"
   "tya"  "ちゃ"
   "tye"  "ちぇ"
   "tyi"  "ちぃ"
   "tyo"  "ちょ"
   "tyu"  "ちゅ"
   "u"    "う"
   "va"   "ゔぁ"
   "ve"   "ゔぇ"
   "vi"   "ゔぃ"
   "vo"   "ゔぉ"
   "vu"   "ゔ"
   "vya"  "ゔゃ"
   "vye"  "ゔぇ"
   "vyi"  "ゔぃ"
   "vyo"  "ゔょ"
   "vyu"  "ゔゅ"
   "wa"   "わ"
   "we"   "うぇ"
   "wha"  "うぁ"
   "whe"  "うぇ"
   "whi"  "うぃ"
   "who"  "うぉ"
   "whu"  "う"
   "wi"   "うぃ"
   "wo"   "を"
   "wu"   "う"
   "wye"  "ゑ"
   "wyi"  "ゐ"
   "xa"   "ぁ"
   "xe"   "ぇ"
   "xi"   "ぃ"
   "xka"  "ヵ"
   "xke"  "ヶ"
   "xn"   "ん"
   "xo"   "ぉ"
   "xtsu" "っ"
   "xtu"  "っ"
   "xu"   "ぅ"
   "xwa"  "ゎ"
   "xya"  "ゃ"
   "xye"  "ぇ"
   "xyi"  "ぃ"
   "xyo"  "ょ"
   "xyu"  "ゅ"
   "ya"   "や"
   "ye"   "いぇ"
   "yo"   "よ"
   "yu"   "ゆ"
   "za"   "ざ"
   "ze"   "ぜ"
   "zi"   "じ"
   "zo"   "ぞ"
   "zu"   "ず"
   "zya"  "じゃ"
   "zye"  "じぇ"
   "zyi"  "じぃ"
   "zyo"  "じょ"
   "zyu"  "じゅ"})

(def max-romaji-length (apply max (map count (keys romaji-table))))

(defprotocol IJapaneseText
  (hiragana? [text])
  (half-width-katakana? [text])
  (full-width-katakana? [text])
  (katakana? [text])
  (kana? [text])
  (kanji? [text])
  (half-width-lowercase-letter? [text])
  (half-width-uppercase-letter? [text])
  (full-width-lowercase-letter? [text])
  (full-width-uppercase-letter? [text])
  (half-width-letter? [text])
  (full-width-letter? [text])
  (letter? [text])
  (half-width-digit? [text])
  (full-width-digit? [text])
  (digit? [text])
  (romaji? [text]) ; TODO: Add support for different romanization types
  (->dakuten [text])
  (->handakuten [text])
  (full-width-hiragana->full-width-katakana [text])
  (full-width-katakana->full-width-hiragana [text]))

#?(:clj
   (extend-protocol IJapaneseText
     Character
     (hiragana? [c] (ch/hiragana? c))
     (half-width-katakana? [c] (ch/half-width-katakana? c))
     (full-width-katakana? [c] (ch/full-width-katakana? c))
     (katakana? [c] (ch/katakana? c))
     (kana? [c] (ch/kana? c))
     (kanji? [c] (ch/kanji? c))
     (half-width-lowercase-letter? [c] (ch/half-width-lowercase-letter? c))
     (half-width-uppercase-letter? [c] (ch/half-width-uppercase-letter? c))
     (full-width-lowercase-letter? [c] (ch/full-width-lowercase-letter? c))
     (full-width-uppercase-letter? [c] (ch/full-width-uppercase-letter? c))
     (half-width-letter? [c] (ch/half-width-letter? c))
     (full-width-letter? [c] (ch/full-width-letter? c))
     (letter? [c] (ch/letter? c))
     (half-width-digit? [c] (ch/half-width-digit? c))
     (full-width-digit? [c] (ch/full-width-digit? c))
     (digit? [c] (ch/digit? c))
     (romaji? [c] (ch/romaji? c))
     (full-width-hiragana->full-width-katakana [c] (ch/full-width-hiragana->full-width-katakana c))
     (full-width-katakana->full-width-hiragana [c] (ch/full-width-katakana->full-width-hiragana c))
     (->dakuten [c] (ch/->dakuten c))
     (->handakuten [c] (ch/->handakuten c))))

(extend-protocol IJapaneseText
  #?(:clj String :cljs string)
  (hiragana? [s] (every-char? ch/hiragana? s))
  (half-width-katakana? [s] (every-char? ch/half-width-katakana? s))
  (full-width-katakana? [s] (every-char? ch/full-width-katakana? s))
  (katakana? [s] (every-char? ch/katakana? s))
  (kana? [s] (every-char? ch/kana? s))
  (kanji? [s] (every-char? ch/kanji? s))
  (half-width-lowercase-letter? [s] (every-char? ch/half-width-lowercase-letter? s))
  (half-width-uppercase-letter? [s] (every-char? ch/half-width-uppercase-letter? s))
  (full-width-lowercase-letter? [s] (every-char? ch/full-width-lowercase-letter? s))
  (full-width-uppercase-letter? [s] (every-char? ch/full-width-uppercase-letter? s))
  (half-width-letter? [s] (every-char? ch/half-width-letter? s))
  (full-width-letter? [s] (every-char? ch/full-width-letter? s))
  (letter? [s] (every-char? ch/letter? s))
  (half-width-digit? [s] (every-char? ch/half-width-digit? s))
  (full-width-digit? [s] (every-char? ch/full-width-digit? s))
  (digit? [s] (every-char? ch/digit? s))
  (romaji? [s] (every-char? ch/romaji? s))
  (->dakuten [s] (map-string ch/->dakuten s))
  (->handakuten [s] (map-string ch/->handakuten s))
  (full-width-hiragana->full-width-katakana [s] (map-string ch/full-width-hiragana->full-width-katakana s))
  (full-width-katakana->full-width-hiragana [s] (map-string ch/full-width-katakana->full-width-hiragana s))

  #?(:clj Object :cljs object)
  (hiragana? [coll] (every? hiragana? coll))
  (half-width-katakana? [coll] (every? half-width-katakana? coll))
  (full-width-katakana? [coll] (every? full-width-katakana? coll))
  (katakana? [coll] (every? katakana? coll))
  (kana? [coll] (every? kana? coll))
  (kanji? [coll] (every? kanji? coll))
  (half-width-lowercase-letter? [coll] (every? half-width-lowercase-letter? coll))
  (half-width-uppercase-letter? [coll] (every? half-width-uppercase-letter? coll))
  (full-width-lowercase-letter? [coll] (every? full-width-lowercase-letter? coll))
  (full-width-uppercase-letter? [coll] (every? full-width-uppercase-letter? coll))
  (half-width-letter? [coll] (every? half-width-letter? coll))
  (full-width-letter? [coll] (every? full-width-letter? coll))
  (letter? [coll] (every? letter? coll))
  (half-width-digit? [coll] (every? half-width-digit? coll))
  (full-width-digit? [coll] (every? full-width-digit? coll))
  (digit? [coll] (every? digit? coll))
  (romaji? [coll] (every? romaji? coll))
  (->dakuten [coll] (map ->dakuten coll))
  (->handakuten [coll] (map ->handakuten coll))
  (full-width-hiragana->full-width-katakana [coll] (map full-width-hiragana->full-width-katakana coll))
  (full-width-katakana->full-width-hiragana [coll] (map full-width-katakana->full-width-hiragana coll))

  ; Return reasonable defaults when dispatching on `nil` rather than throwing an exception.
  nil
  (hiragana? [_] false)
  (half-width-katakana? [_] false)
  (full-width-katakana? [_] false)
  (katakana? [_] false)
  (kana? [_] false)
  (kanji? [_] false)
  (half-width-lowercase-letter? [_] false)
  (half-width-uppercase-letter? [_] false)
  (full-width-lowercase-letter? [_] false)
  (full-width-uppercase-letter? [_] false)
  (half-width-letter? [_] false)
  (full-width-letter? [_] false)
  (letter? [_] false)
  (half-width-digit? [_] false)
  (full-width-digit? [_] false)
  (digit? [_] false)
  (romaji? [_] false)
  (->dakuten [_])
  (->handakuten [_])
  (full-width-hiragana->full-width-katakana [_])
  (full-width-katakana->full-width-hiragana [_]))

(defn- get-mora [s i]
  (loop [n (min max-romaji-length (- (count s) i))]
    (if (<= n 0)
      ["" 1]
      (let [romaji (subs s i (+ i n))]
        (if-let [mora (get romaji-table romaji)]
          [mora n]
          (if (> n 1)
            (recur (dec n))
            [(subs s i (inc i)) 1]))))))

(defn romaji->hiragana [s]
  (let [buffer (StringBuffer.)]
    (loop [i 0]
      (let [[mora ^long advance] (get-mora s i)]
        (.append buffer mora)
        (let [j (+ i advance)]
          (when (< j (count s))
            (recur j)))))
    (str buffer)))
