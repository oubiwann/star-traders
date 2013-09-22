(ns starlanes.player
  (:require [starlanes.util :as util]))


(defn player-data-factory []
  "Elements of stock will be maps, each having an entry for the company name
  and the number of shares held for that company."
  {:name ""
   :cash 0.0
   :stock []})

(defn create-new-player [index]
  (let [prompt (str "Player " (str (inc index)) ", what is your name? ")
        player-name (util/input prompt)]
    (conj (player-data-factory) {:name player-name})))

; XXX implement a validator for player count; an error shouldn't be thrown
; when a user doesn't enter an integer. With bad input, retry. Also,
; this should check for maximum number of players, too.
(defn validate-player-count [input]
  )

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

; XXX fix up parameters to display instructions for first-time play, or
; display a "continue" prompt for a play-again scenario...
(defn determine-player-order
  [game-data]
  (let [game-data (set-player-order game-data)]
    (util/display (str "The order of play is:" \newline))
    (print-player-order game-data)
    game-data))
