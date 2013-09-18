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

(defn get-total-possible-move-count
  "The total number of moves a player can have presented to them during a turn
  (or, more precisely, during their move of a turn) is determined by a few
  things:

    1) the max number of moves allowed (game configuration constant)
    2) the number of available spaces left on the board (unclaimed
       space, marked by default with a 'dot' or 'period')."
  ([game-data]
    (get-total-possible-move-count
      const/win-by-turns?
      (get-remaining-moves game-data)
      const/max-moves-choices
      game-data))
  ([win-by-turns? remaining-moves max-moves-choices game-data]
    (cond
      win-by-turns?
      (cond
        ; max-moves-choices is the number of move options presented to the user,
        ; and we want to be sure not to offer more move options than are
        ; actually available
        (< remaining-moves max-moves-choices)
          remaining-moves
        :else max-moves-choices)
      :else remaining-moves)))

(defn get-moves [game-data]
  (let [open-coords (game-map/get-open-coords game-data)]
    (take const/max-moves-choices (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn get-character-for-move [game-data move]
  "+")

(defn get-remaining-moves-for-turn
  ([game-data]
    (let [max-moves-per-turn (util/get-player-count game-data)
          current-move-index (get-current-move-index game-data)]
      (get-remaining-moves-for-turn max-moves-per-turn current-move-index)))
  ([max-moves-per-turn current-move-index]
    (- max-moves-per-turn current-move-index)))

(defn get-remaining-moves-for-game
  ([game-data]
    (get-remaining-moves-for-game
      const/win-by-turns?
      (util/get-max-total-moves game-data)
      (get-remaining-moves game-data)
      (game-data :total-moves)))
  ([win-by-turns? max-by-turn-moves max-moves total-moves]
    (cond
      win-by-turns?
        (cond
          (< total-moves max-by-turn-moves)
            total-moves
          :else 0)
      :else max-moves)))

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
    (> (-moves-remain? game-data) 0) true
    :else false))

