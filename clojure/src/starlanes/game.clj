(ns starlanes.game
  (:require [clojure.math.combinatorics :as combi]
            [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.util :as util]))


(defn game-data-factory []
  {:star-map (sorted-map),
   :total-moves 0,
   :players [],
   :player-order [],
   :move 0,
   :companies [],
   :share-value {},
   :rand (util/random const/seed)})

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

(defn create-star-map-for-game [game-data]
  (let [star-map (create-star-map game-data)]
    (conj game-data {:star-map star-map})))

(defn update-coords
  "Return a new game-data data structure with a new item at the given
  coordinates."
  [x y coord-item game-data]
  (let [y (const/make-y-coord y)
        coord-key (keyword (str x y))
        old-star-map (game-data :star-map)
        new-star-map (conj old-star-map [coord-key coord-item])]
    (conj game-data {:star-map new-star-map})))

(defn inc-move
  "Return a new game-data data structure with the total move count incremented."
  [game-data]
  (conj
    game-data
    {:total-moves (inc (game-data :total-moves))}))

(defn get-empty-coord [coord-data]
  (if (util/coord-open? coord-data (const/items :empty))
    (first coord-data)))

(defn get-open-coords [game-data]
  (remove
    nil?
    (map
      get-empty-coord
      (game-data :star-map))))

(defn get-total-possible-move-count [game-data]
  (let [open-coords (count (get-open-coords game-data))]
    (cond
      const/win-by-turns?
      (cond
        (>= open-coords const/max-moves)
        const/max-moves
        :else open-coords)
      :else open-coords)))

(defn get-moves [game-data]
  (let [open-coords (get-open-coords game-data)]
    (take const/max-moves (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn print-moves [game-data]
  (util/display
    (str
      (string/join
        \space
        (get-friendly-moves game-data))
      \newline)))

(defn get-item-coords [item-char game-data]
  (remove
    nil?
    (map
      #(util/filter-item % item-char)
      (game-data :star-map))))

(defn get-star-coords [game-data]
  (get-item-coords (const/items :star) game-data))

(defn get-outpost-coords [game-data]
  (get-item-coords (const/items :outpost) game-data))

(defn near-item? [neighbors all-item-coords]
  )

(defn next-to-star? [neighbors game-data]
  )

(defn next-to-outpost? [neighbors game-data]
  )

(defn next-to-company? [neighbors game-data]
  )

(defn get-valid-x-coord-components [coords]
  (util/get-valid-coord-range
    coords
    const/xgrid-start
    const/xgrid-end))

(defn get-valid-y-coord-components [coords]
  (util/get-valid-coord-range
    coords
    const/ygrid-start
    const/ygrid-end))

(defn -get-possible-coords [coord filter-fn]
  (filter-fn
    [(dec coord) coord (inc coord)]))

(defn -get-possible-x-coords [coords]
  )

(defn get-neighbors
  "This function takes a keyword that represents a coordinate (e.g., :b23) and
   returns the coordinates for the neighboring positions. The number of returned
   neighbors could range anywhere from 4 (when the given coorindate is in a
   corner of the map) to 8 (when the given coordinate is in the center of the
   map).
  "
  [coord]
  )


