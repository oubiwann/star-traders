(ns starlanes.util
  (:require [clojure.string :as string]
            [clojure.set :refer [intersection]]))

(def fake-game-data
  {:star-map
    {:a1 ".", :a2 ".", :a3 ".", :a4 ".", :a5 ".", :b1 ".", :b2 ".",
     :b3 ".", :b4 ".", :b5 ".", :c1 ".", :c2 ".", :c3 "*", :c4 ".",
     :c5 ".", :d1 ".", :d2 ".", :d3 ".", :d4 ".", :d5 ".", :e1 ".",
     :e2 ".", :e3 ".", :e4 ".", :e5 "."},
   :total-moves 0,
   :players [{:stock nil, :name "Alice", :cash 0.0}],
   :player-order [0],
   :move 0,
   :companies [],
   :share-value {},
   :rand nil})

(defn display [data]
  (.print (System/out) data))

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

(defn random [seed]
  (proxy [java.util.Random][seed]
    (next [a] (proxy-super next a))))

(defn rand-float [random]
  (.nextFloat random))

(defn rand-game [game-data]
  (rand-float (:rand game-data)))

(defn coord-open? [coord empty-string]
  (cond
    (= (last coord) empty-string) true
    :else false))

(defn keyword->xy
  "Given a coordinate in keyword-form, return the x and y components as a
   vector."
  [keyword-coord]
  (let [string-name (name keyword-coord)
        string-len (count string-name)
        x-coord (take 1 string-name)
        y-coord (take-last (dec string-len) string-name)]
    [(string/join x-coord)
     (string/join y-coord)]))

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

(defn get-valid-coord-range
  "Given the integer representation of a coordinate component (e.g., either a
   value for x or a value for y, not both), as well as the minimum and maximum
   possible values for the axis of the given component (e.g., x- or y-axis),
   XXX
   "
  [coords min max]
  (filter-allowed
    coords
    (range min (inc max))))



