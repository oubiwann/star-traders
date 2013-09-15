(ns starlanes.game.command
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.finance :as finance]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(defn display-moves [moves game-data]
  (util/display
    (str
      \newline
      ((player/get-current-player game-data) :name)
      ", here are your legal moves for this turn:"
      \newline
      "  "
      (string/join \space moves)
      \newline)))

(defn display-player-order [game-data]
  (util/display (str \newline))
  (player/print-player-order game-data)
  (util/display (str \newline))
  (util/input const/continue-prompt)
  nil)

(defn display-score [game-data]
  nil)

(defn display-help []
  nil)

(defn display-commands []
  nil)

(defn save-game [game-data]
  (let [filename (util/input
                    (str \newline
                         "Enter the filename for the game data: "))]
    (util/display
      (str \newline
           "Saving game to '" filename "' ..."
           \newline \newline))
    (spit filename (util/serialize-game-data game-data))
    (util/input const/continue-prompt)
    nil))

(defn -load-game []

  (let [filename (util/input
                   (str \newline
                        "Ennter the saved game file you want to load: "))
        game-data (read-string (slurp filename))]
    (util/display
      (str \newline
           "Loaded game-data from '" filename "'."
           \newline \newline))
    (util/input const/continue-prompt)
    game-data))

(defn load-game []
  (let [check (util/input
                (str \newline
                     "This will overwrite your current game."
                     const/confirm-prompt))]
    (cond
      (= (string/lower-case check) "y") (-load-game))))

(defn quit-game [cleanup-fn game-data]
  (util/display (str \newline
                     "You have asked to quit the game." \newline))
  (let [answer (util/input const/confirm-prompt)]
    (cond
      (= answer "y") (do (cleanup-fn game-data) (util/exit)))))
