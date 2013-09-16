(ns starlanes.player
  (:require [starlanes.game.movement :as game-move]
            [starlanes.util :as util]))


(defn player-data-factory []
  {:name "",
   :cash 0.0,
   :stock nil})

(defn create-new-player [index]
  (let [prompt (str "Player " (str (inc index)) ", what is your name? ")
        player-name (util/input prompt)]
    (conj (player-data-factory) {:name player-name})))

(defn get-new-players
  ([]
    ; XXX add type-checking/catching to util/input?
    (get-new-players (Integer. (util/input "How many players? "))))
  ([player-count]
    (for [index (range player-count)]
      (create-new-player index))))

(defn set-player-order [game-data]
  (let [player-count (util/get-player-count game-data)
        indices (range player-count)]
    (conj game-data {:player-order (shuffle indices)})))

(defn get-players-in-order [game-data]
  (let [players (game-data :players)
        order (game-data :player-order)]
    (map #(nth players %) order)))

(defn print-player-order [game-data]
  (doseq [player (get-players-in-order game-data)]
    (util/display (str \tab (player :name) \newline))))

(defn determine-player-order [game-data]
  (let [game-data (set-player-order game-data)]
    (util/display (str "The order of play is:" \newline))
    (print-player-order game-data)
    game-data))

(defn get-current-player-index [game-data]
  (nth
    (game-data :player-order)
    (game-move/get-current-move-index game-data)))

(defn get-current-player [game-data]
  (nth
    (game-data :players)
    (get-current-player-index game-data)))
