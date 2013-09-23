(ns starlanes.game.map
  (:require [clojure.math.combinatorics :as combi]
            [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.util :as util]))


(defn create-item [rand-float]
  (cond
    (<= rand-float const/star-rate)
    (const/items :star)
    :else (const/items :empty)))

(defn create-game-item [game-data]
  (create-item
    (util/rand-game game-data)))

(defn -create-star-map [game-data]
  (map
    #(reverse (conj % (create-game-item game-data)))
    (combi/cartesian-product const/ygrid const/xgrid)))

(defn -combine-coords
  ([data]
    (apply -combine-coords data))
  ([x y item]
    (let [y (const/make-y-coord y)]
      [(keyword (str x y)) item])))

(defn create-star-map [game-data]
  (let [star-map-data (-create-star-map game-data)]
    (into
      (sorted-map)
      (map
        -combine-coords
        star-map-data))))

(defn update-coords
  "Return a new game-data data structure with a new item at the given
  coordinates."
  [keyword-coord coord-item game-data]
  (let [old-star-map (game-data :star-map)
        new-star-map (conj old-star-map [keyword-coord coord-item])]
    (conj game-data {:star-map new-star-map})))

(defn multi-update-coords
  ""
  [keyword-coords coord-item game-data]
  (let [keyword-coord (first keyword-coords)
        remaining (rest keyword-coords)]
  (cond
    (not (nil? keyword-coord))
      (multi-update-coords
        remaining
        coord-item
        (update-coords keyword-coord coord-item game-data))
    :else game-data)))

(defn get-empty-coord [coord-data]
  (if (util/coord-open? coord-data (const/items :empty))
    (first coord-data)))

(defn get-open-coords [game-data]
  (remove
    nil?
    (map
      get-empty-coord
      (game-data :star-map))))

(defn get-item [keyword-coord game-data]
  ((game-data :star-map) keyword-coord))

(defn get-item-coords [item-char game-data]
  (remove
    nil?
    (map
      #(first (util/filter-item % item-char))
      (game-data :star-map))))

(defn get-company-coords [game-data]
  (sort
    (flatten
      (map
        (fn [x] (get-item-coords x game-data))
        (util/get-companies-letters)))))

(defn get-star-coords [game-data]
  (get-item-coords (const/items :star) game-data))

(defn get-outpost-coords [game-data]
  (get-item-coords (const/items :outpost) game-data))

(defn get-possible-neighbors [coord]
    [(dec coord) coord (inc coord)])

(defn get-possible-x-neighbors
  "This function takes a letter representing the x component of a coordinate
  pair and returns the legal neighbors."
  [x-coord]
  (filter
    util/x-coord?
    (map
      util/chr
      (get-possible-neighbors
        (util/ord x-coord)))))

(defn get-possible-y-neighbors
  "This function takes a number representing the y component of a coordinate
  pair and returns the legal neighbors."
  [y-coord]
  (filter
    util/y-coord?
    (get-possible-neighbors (Integer. y-coord))))

(defn get-neighbors-pairs
  "This function takes a keyword that represents a coordinate (e.g., :b23) and
  returns coordinates (pair-wise, e.g., [(x1 y1) (x2 y2) ...]) for the
  neighboring positions. The number of returned neighbors could range anywhere
  from 3 (when the given coorindate is in a corner of the map) to 8 (when the
  given coordinate is in the center of the map).
  "
  [keyword-coord]
  (let [[x-coord y-coord] (util/keyword->xy keyword-coord)
        x-neighbors (get-possible-x-neighbors x-coord)
        y-neighbors (get-possible-y-neighbors y-coord)
        pairs (combi/cartesian-product x-neighbors y-neighbors)]
    (remove #{[x-coord (Integer. y-coord)]} pairs)))

(defn get-neighbors
  "This function takes a keyword that represents a coordinate (e.g., :b23) and
  returns coordinates (as a list of keywords, e.g., [:x1y1 :x2y2 ...]) for the
  neighboring positions. The number of returned neighbors could range anywhere
  from 3 (when the given coorindate is in a corner of the map) to 8 (when the
  given coordinate is in the center of the map).
  "
  [keyword-coord]
  (map
    (comp keyword string/join)
    (get-neighbors-pairs keyword-coord)))

(defn get-item-neighbors [keyword-coord game-data]
  (map (fn [x] [x (get-item x game-data)]) (get-neighbors keyword-coord)))

(defn company? [item]
  (cond
    (util/in? (util/get-companies-letters) item) true
    :else false))

(defn star? [item]
  (cond
    (= item (const/items :star)) true
    :else false))

(defn outpost? [item]
  (cond
    (= item (const/items :outpost)) true
    :else false))

(defn get-neighbor-companies [keyword-coord game-data]
  (filter (comp company? second) (get-item-neighbors keyword-coord game-data)))

(defn get-neighbor-stars [keyword-coord game-data]
  (filter (comp star? second) (get-item-neighbors keyword-coord game-data)))

(defn get-neighbor-outposts [keyword-coord game-data]
  (filter (comp outpost? second) (get-item-neighbors keyword-coord game-data)))

(defn near-item? [keyword-coord coords-for-items]
  (let [items-neighbors (flatten (map get-neighbors coords-for-items))]
    (util/in? items-neighbors keyword-coord)))

(defn next-to-company? [keyword-coord game-data]
  (near-item? keyword-coord (get-company-coords game-data)))

(defn next-to-star? [keyword-coord game-data]
  (near-item? keyword-coord (get-star-coords game-data)))

(defn next-to-outpost? [keyword-coord game-data]
  (near-item? keyword-coord (get-outpost-coords game-data)))




