(ns starlanes.player
  (:require [starlanes.game :as game]
            [starlanes.util :as util]))


(defn player-data-factory []
  {:name "",
   :cash 0.0,
   :stock nil})

(defn create-new-player [index]
  (let [prompt (str "Player " (str (+ index 1)) ", what is your name? ")
        player-name (util/input prompt)]
    (conj (player-data-factory) {:name player-name})))

(defn get-players [game-data]
  (game-data :players))

(defn get-player-count [game-data]
  (count (get-players game-data)))

(defn get-new-players
  ([]
    ; XXX add type-checking/catching to util/input?
    (get-new-players (Integer. (util/input "How many players? "))))
  ([player-count]
    (for [index (range player-count)]
      (create-new-player index))))

(defn set-new-players
  ([]
    (set-new-players (game/game-data-factory)))
  ([game-data]
    (conj game-data {:players (doall (get-new-players))})))

(defn get-player-names [game-data]
  )

(defn get-current-player [game-data]
  )

(defn set-player-order [game-data]
  (let [player-count (get-player-count game-data)
        indices (range player-count)]
    (conj game-data {:player-order (shuffle indices)})))

(defn print-player-order [game-data]
  (doseq [player-index (game-data :player-order)]
    (let [player (nth (game-data :players) player-index)]
      (util/display (str "\t" (player :name) \newline)))))

(defn determine-player-order [game-data]
  (let [game-data (set-player-order game-data)]
    (util/display (str "The order of play is:" \newline))
    (print-player-order game-data)
    game-data))