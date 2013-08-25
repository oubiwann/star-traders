(ns starlanes.layout
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.game :as game]
            [starlanes.util :as util]))


(def grid-space (util/mult-str const/grid-spaces \space))

(defn get-header []
  (string/join const/row-heading-init const/xgrid))

(defn get-row-header [row-grid-entry]
  (str row-grid-entry const/row-heading-init))

(defn get-row-header-buffer []
  (count
    (get-row-header
        (first
            (take-last 1 const/ygrid)))))

(defn print-grid-title [buffer-width grid-width]
  (let [mid-point (/ grid-width 2)
        title-len (count const/game-title)
        start-point (+ 1 (- mid-point (/ title-len 2)))
        fill (util/mult-str (int start-point) \space)
        buffer (util/mult-str buffer-width \space)
        separator (util/mult-str title-len const/horiz-title-heading-char)]
    (util/display (str buffer fill const/game-title \newline))
    (util/display (str buffer fill separator \newline))))

(defn print-xgrid-headers [buffer-length]
  (let [header (get-header)
        separator (util/mult-str (count header) const/horiz-divider-char)]
    (util/display (str (util/mult-str buffer-length \space)
                       header
                       \newline))
    (util/display (str (util/mult-str (- buffer-length 2) \space)
                       const/horiz-divider-init
                       separator
                       const/horiz-divider-term
                       \newline))))

(defn print-footer [buffer-length footer-length]
  (util/display (str (util/mult-str (- buffer-length 2) \space)
                     const/horiz-divider-init
                     (util/mult-str footer-length const/horiz-divider-char)
                     const/horiz-divider-term
                     \newline)))

(defn get-row-string [row-data]
  "'row-data' contains a list of keys (keywords) and values. To get the string
  content for the row, the values need to be extracted.
  "
  (let [item-data (vals row-data)]
    (string/join grid-space item-data)))

(defn get-row [row-key grouped-star-map]
  (let [row-data (sort (get grouped-star-map row-key))]
    (get-row-string row-data)))

(defn keyword-grouper
  "This function expects a list whose first element is a keyword (which will be
  used to group the associated data).

  The whole data structure that is being sorted is a (seq ...) of a map, a list
  of lists, where the sub-lists are pairs of keywords and single-character
  string values."
  [item]
  (str
    (first
      (name
        (first item)))))

(defn grouper
  "This function expects a list whose first element is a keyword, and whose
  second element is a character (star-map 'item'). It is the second item by
  which the associated data will be grouped.

  'join' is used because there may be more than one digit after 'drop' is
  called -- which would result in a sequence being returned (and that would
  need to be 'join'ed).

  The whole data structure that is being sorted is a (seq ...) of a map, a list
  of lists, where the sub-lists are pairs of keywords and single-character
  string values."
  [item]
  (string/join
    (drop 1
      (name
        (first item)))))

(defn print-rows [game-data]
  (let [star-map (group-by grouper (game-data :star-map))]
    (doseq [[row-num row-data] (into (sorted-map) star-map)]
      (util/display
        (str (get-row-header row-num)
             (get-row-string row-data)
             const/row-heading-term
             \newline)))))

(defn draw-grid [game-data]
  (util/clear-screen)
  (let [buffer-length (get-row-header-buffer)
        header-length (count (get-header))]
    (print-grid-title buffer-length header-length)
    (print-xgrid-headers buffer-length)
    (print-rows game-data)
    (print-footer buffer-length header-length)))

(defn draw-new-grid [game-data]
  (let [game-data (game/create-star-map-for-game game-data)]
    (draw-grid game-data)))