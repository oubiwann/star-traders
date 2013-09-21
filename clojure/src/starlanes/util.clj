(ns starlanes.util
  (:require [clojure.string :as string]
            [clojure.set :refer [intersection]]
            [starlanes.const :as const]))


(def fake-game-data
  {:star-map
    {:a1 "*", :a2 ".", :a3 ".", :a4 ".", :a5 ".",
     :b1 ".", :b2 ".", :b3 ".", :b4 ".", :b5 ".",
     :c1 ".", :c2 ".", :c3 "*", :c4 ".", :c5 "+",
     :d1 ".", :d2 ".", :d3 ".", :d4 ".", :d5 ".",
     :e1 ".", :e2 ".", :e3 ".", :e4 "*", :e5 "."},
   :total-moves 0,
   :players [
    {:stock nil, :name "Alice", :cash 0.0},
    {:stock nil, :name "Bob", :cash 0.0},
    {:stock nil, :name "Carol", :cash 0.0}],
   :player-order [1 0],
   :move 0,
   :companies-queue ["Al" "Be" "Ca" "De" "Er"],
   :companies [],
   :share-value {},
   :rand nil})

(defn display [data]
  (.print (System/out) data))

(defn exit []
  (display (str \newline "Shutting down game ... " \newline))
  (System/exit 0))

(defn clear-screen []
  (display "\u001b[2J")
  (display "\u001B[0;0f"))

(defn input [prompt]
  (display prompt)
  (read-line))

(defn mult-str [amount string]
  (string/join (repeat amount string)))

(defn ord [chr]
  (int (.charAt chr 0)))

(defn chr [ord]
  (str (char ord)))

(defn random [seed]
  (proxy [java.util.Random][seed]
    (next [a] (proxy-super next a))))

(defn rand-float [random]
  (.nextFloat random))

(defn rand-game [game-data]
  (rand-float (game-data :rand)))

(defn coord-open? [coord empty-string]
  (cond
    (= (last coord) empty-string) true
    :else false))

(defn string->xy
  "Given a string coordinate, return the x and y components as a vector."
  [string-coord]
  (let [string-len (count string-coord)
        x-coord (take 1 string-coord)
        y-coord (take-last (dec string-len) string-coord)]
    [(string/join x-coord)
     (string/join y-coord)]))

(defn keyword->xy
  "Given a coordinate in keyword-form, return the x and y components as a
   vector."
  [keyword-coord]
  (let [string-name (name keyword-coord)
        [x-coord y-coord] (string->xy string-name)]
    [x-coord y-coord]))

(defn xy->keyword
  "Given a sequence of two items, each representing an x and y value for a
  coordinate, convert to a keyword."
  [xy-pair]
  (keyword
    (string/join xy-pair)))

(defn move->string-coord
  "Give a player move (a string of the form 'yx'), convert it to a string
  coordinate (a string of the form 'xy')."
  [move]
  (let [x-coord (str (last move))
        [y-coord] (string/split move #"[a-z]")]
    (str x-coord y-coord)))

(defn move->keyword
  "Given a player move coordinate (a string of the form 'yx') convert to a
  keyword coordinate (of the form :xy)."
  [move]
  (let [xy-coords (string->xy (move->string-coord move))]
    (xy->keyword xy-coords)))

(defn get-friendly-coord
  "Given a coord (a keyword such as :a23), return a format that is easier for
  a player to read (by row, then columns)."
  [keyword-coord]
  (let [[x-coord y-coord] (keyword->xy keyword-coord)]
    (string/join [y-coord x-coord])))

(defn is-item? [coord-data expeted-item-char]
  (cond
    (= (last coord-data) expeted-item-char) true
    :else false))

(defn filter-item
  "This function is intended to be used as a parameter passed to a map
  function."
  [coord-data expeted-item-char]
  (cond
    (is-item? coord-data expeted-item-char) coord-data
    :else nil))

(defn filter-allowed [all legal]
  (intersection (set all) (set legal)))

(defn get-x-coord-range []
  (map
    chr
    (range
      const/xgrid-start
      const/xgrid-end)))

(defn get-y-coord-range []
  (range
    const/ygrid-start
    const/ygrid-end))

(defn in?
  "Given a sequence and a potential element of that sequence, determine if it
  is, in fact, part of that sequence."
  [sequence item]
  (if (empty? sequence)
    false
    (reduce
      #(or %1 %2)
      (map
        #(= %1 item)
        sequence))))

(defn x-coord? [x-coord]
  (in? (get-x-coord-range) x-coord))

(defn y-coord? [y-coord]
  (in? (get-y-coord-range) y-coord))

(defn serialize-game-data [game-data]
  (conj game-data {:rand nil}))

(defn get-players [game-data]
  (game-data :players))

(defn get-player-count [game-data]
  (count (get-players game-data)))

(defn get-max-total-moves
  "This function should only be used when when win-by-turns? is set to 'true'."
  ([game-data]
    (get-max-total-moves const/max-turns (get-player-count game-data)))
  ([max-turns player-count]
    (* max-turns player-count)))


