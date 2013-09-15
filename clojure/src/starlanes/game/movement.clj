(ns starlanes.game.movement
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.game.map :as game-map]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(defn inc-move
  "Return a new game-data data structure with the total move count incremented."
  [game-data]
  (conj
    game-data
    {:total-moves (inc (game-data :total-moves))}))

(defn get-total-possible-move-count [game-data]
  (let [open-coords (count (game-map/get-open-coords game-data))]
    (cond
      const/win-by-turns?
      (cond
        (>= open-coords const/max-moves)
        const/max-moves
        :else open-coords)
      :else open-coords)))

(defn get-moves [game-data]
  (let [open-coords (game-map/get-open-coords game-data)]
    (take const/max-moves (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn get-character-for-move [game-data move]
  "+")

(defn check-remaining-moves [available-moves game-data]
  ; XXX get the number of moves requires for each player to move during the
  ;     current turn
  ; XXX if that is greater than the current number of moves left, gp into
  ;     end-game mode, displaying message about the reason for the end
  #_(tally-scores game-data)
  #_(util/exit))

