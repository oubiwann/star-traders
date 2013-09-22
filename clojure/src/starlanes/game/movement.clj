(ns starlanes.game.movement
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.game.map :as game-map]
            [starlanes.finance :as finance]
            [starlanes.util :as util]))


(defn inc-move
  "Return a new game-data data structure with the total move count incremented."
  [game-data]
  (conj
    game-data
    {:total-moves (inc (game-data :total-moves))}))

(defn legal?
  "A predicate useful for determining validity of entered player moves."
  [available-moves move]
  (util/in? available-moves move))

(defn get-remaining-moves [game-data]
  (count (game-map/get-open-coords game-data)))

(defn get-current-move-index [game-data]
  (mod
    (game-data :total-moves)
    (util/get-player-count game-data)))

(defn get-current-player-index [game-data]
  (nth
    (game-data :player-order)
    (get-current-move-index game-data)))

(defn get-current-player [game-data]
  (nth
    (game-data :players)
    (get-current-player-index game-data)))

(defn get-moves [game-data]
  (let [open-coords (game-map/get-open-coords game-data)]
    (take const/max-moves-choices (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn update-map-with-move
  "Return new game data with a contained star-map that takes into consideration
  the move just passed."
  [move game-data]
  (let [keyword-coord (util/move->keyword move)]
    (cond
      ; is the move next to a company?
      (game-map/next-to-company? keyword-coord game-data)
        (finance/perform-company-merger
          keyword-coord (get-current-player game-data) game-data)
      ; more than one company?
      ; which company has the highest value?
      ; is the move next to a star?
      (game-map/next-to-star? keyword-coord game-data)
        (finance/create-star-company
          keyword-coord (get-current-player game-data) game-data)
      ; is the move next to an outpost?
      (game-map/next-to-outpost? keyword-coord game-data)
        (finance/create-outpost-company
          keyword-coord (get-current-player game-data) game-data)
      ; if no neighbors at all, make it an outpost
      :else
        (game-map/update-coords
          keyword-coord
          (const/items :outpost)
          game-data))))

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

