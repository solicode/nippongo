(ns nippongo.syllabary)

(def rows [:a :ka :sa :ta :na :ha :ma :ya :ra :wa :ga :za :da :ba :pa])
(def columns [:a :i :u :e :o])
(def syllabary-width (count columns))

(def syllabary
  [; hiragana
   \あ \い \う \え \お
   \か \き \く \け \こ
   \さ \し \す \せ \そ
   \た \ち \つ \て \と
   \な \に \ぬ \ね \の
   \は \ひ \ふ \へ \ほ
   \ま \み \む \め \も
   \や ::none \ゆ ::none \よ
   \ら \り \る \れ \ろ
   \わ \ゐ ::none \ゑ \を
   \が \ぎ \ぐ \げ \ご ; 濁音
   \ざ \じ \ず \ぜ \ぞ
   \だ \ぢ \づ \で \ど
   \ば \び \ぶ \べ \ぼ
   \ぱ \ぴ \ぷ \ぺ \ぽ
   ; katakana
   \ア \イ \ウ \エ \オ
   \カ \キ \ク \ケ \コ
   \サ \シ \ス \セ \ソ
   \タ \チ \ツ \テ \ト
   \ナ \ニ \ヌ \ネ \ノ
   \ハ \ヒ \フ \ヘ \ホ
   \マ \ミ \ム \メ \モ
   \ヤ ::none \ユ ::none \ヨ
   \ラ \リ \ル \レ \ロ
   \ワ \ヰ ::none \ヱ \ヲ
   \ガ \ギ \グ \ゲ \ゴ ; 濁音
   \ザ \ジ \ズ \ゼ \ゾ
   \ダ \ヂ \ヅ \デ \ド
   \バ \ビ \ブ \ベ \ボ
   \パ \ピ \プ \ペ \ポ])

(defn index-of [coll val]
  (loop [i 0, items coll]
    (cond
      (empty? items) -1
      (= (first items) val) i
      :else (recur (inc i) (rest items)))))

(defn shift-row [c row]
  (let [s-index (index-of syllabary c)]
    (if (neg? s-index)
      c
      (let [target-row-index (index-of rows row)]
        (if (neg? target-row-index)
          c
          (let [current-row-index (quot s-index syllabary-width)
                shift-needed (* (- target-row-index current-row-index) syllabary-width)]
            (nth syllabary (+ s-index shift-needed))))))))

(defn shift-column [c column]
  (let [s-index (index-of syllabary c)]
    (if (neg? s-index)
      c
      (let [target-column-index (index-of columns column)]
        (if (neg? target-column-index)
          c
          (let [current-column-index (rem s-index syllabary-width)
                shift-needed (- target-column-index current-column-index)]
            (nth syllabary (+ s-index shift-needed))))))))

(defn row&column [c]
  "Gets the row (行) and column (段) of a character."
  (let [s-index (index-of syllabary c)]
    (when-not (neg? s-index)
      [(nth rows (quot s-index syllabary-width))
       (nth columns (rem s-index syllabary-width))])))

(defn row [c]
  (let [s-index (index-of syllabary c)]
    (when-not (neg? s-index)
      (nth rows (quot s-index syllabary-width)))))

(defn column [c]
  (let [s-index (index-of syllabary c)]
    (when-not (neg? s-index)
      (nth columns (rem s-index syllabary-width)))))

(defn char-of [row column]
  (let [row-index (index-of rows row)
        column-index (index-of columns column)]
    (when (and (>= row-index 0) (>= column-index 0))
      (let [s-index (+ (* row-index syllabary-width) column-index)]
        (nth syllabary s-index)))))

(defn row-chars [row]
  (let [s-index (* (index-of rows row) syllabary-width)]
    (when-not (neg? s-index)
      (remove #(= % ::none) (take syllabary-width
                              (drop s-index syllabary))))))

(defn column-chars [column]
  (let [column-index (index-of columns column)]
    (when-not (neg? column-index)
      (remove #(= % ::none) (take-nth syllabary-width
                              (drop column-index syllabary))))))
