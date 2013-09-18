(ns starlanes.game.movement
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.game.map :as game-map]
            [starlanes.util :as util]))


(defn inc-move
  "Return a new game-data data structure with the total move count incremented."
  [game-data]
  (conj
    game-data
    {:total-moves (inc (game-data :total-moves))}))

(defn get-remaining-moves [game-data]
  (count (game-map/get-open-coords game-data)))

(defn get-current-move-index [game-data]
  (mod
    (game-data :total-moves)
    (util/get-player-count game-data)))

(defn get-moves [game-data]
  (let [open-coords (game-map/get-open-coords game-data)]
    (take const/max-moves-choices (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn get-character-for-move [game-data move]
  "+")

(defn -moves-remain?
  ([game-data]
    (-moves-remain?
      const/win-by-turns?
      (util/get-max-total-moves game-data)
      (game-data :total-moves)
      (get-remaining-moves game-data)))
  ([win-by-turns? max-moves moves-so-far remaining]
    (cond
      win-by-turns?
        (cond
          (< moves-so-far max-moves)
            (- max-moves moves-so-far)
          :else 0)
      :else remaining)))

(defn moves-remain? [game-data]
  (cond
    (pos? (-moves-remain? game-data)) true
    :else false))

