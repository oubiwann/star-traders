(ns starlanes.game
  (:use [clojure.math.combinatorics :as combi]
        [starlanes.const :as const]
        [starlanes.util :as util]))


(defn game-data-factory []
  {:star-map (sorted-map),
   :total-moves 0,
   :players [],
   :player-order [],
   :move nil,
   :companies [],
   :share-value {},
   :rand (util/random const/seed)})

(defn create-item [rand-float]
  (cond
    (<= rand-float const/star-rate)
    const/star-char
    :else const/grid-point))

(defn create-game-item [game-data]
  (let [rand-float (util/rand-game game-data)]
    (create-item rand-float)))

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
      (map #(-combine-coords %) star-map-data))))

(defn create-star-map-for-game [game-data]
  (let [star-map (create-star-map game-data)]
    (conj game-data {:star-map star-map})))

(defn update-coords
  "Return a new game-data data structure"
  [x y coord-item game-data]
  (let [y (const/make-y-coord y)
        coord-key (keyword (str x y))
        old-star-map (game-data :star-map)
        new-star-map (conj old-star-map [coord-key coord-item])]
    (conj game-data {:star-map new-star-map})))