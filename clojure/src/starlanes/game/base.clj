(ns starlanes.game.base
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.finance :as finance]
            [starlanes.game.map :as game-map]
            [starlanes.game.movement :as game-move]
            [starlanes.game.command :as game-command]
            [starlanes.instructions :as instructions]
            [starlanes.layout :as layout]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(declare do-player-turn)

(defn game-data-factory []
  {:star-map (sorted-map)
   :total-moves 0
   :players []
   :player-order []
   :move 0
   :companies []
   :companies-queue (util/get-companies)
   :stock-exchange {}
   :rand (util/random const/seed)})

(defn create-star-map-for-game [game-data]
  (let [star-map (game-map/create-star-map game-data)]
    (conj game-data {:star-map star-map})))

(defn set-new-stock-exchange
  ""
  [game-data]
  (let [companies-letters (map first (util-get-companies))
        players-names (map (fn [x] (x :name)) (game-data :players))]
    (conj
      game-data
      {:stock-exchange
        (finance/get-new-exchange
          companies-letters
          players-names)})))

(defn set-new-players
  ([]
    (set-new-players (game-data-factory)))
  ([game-data]
    (conj game-data {:players (doall (player/get-new-players))})))

(defn setup-game [& {:keys [first-time?] :or {first-time? true}}]
  (util/clear-screen)
  (let [game-init (game-data-factory)
        game-with-star-map (create-star-map-for-game game-init)
        game-with-players (set-new-players game-with-star-map)
        game-with-player-order (player/determine-player-order game-with-players)
        game-with-stock-exchange (set-new-stock-exchange game-with-player-order)]
    (cond
      first-time? (instructions/display?)
      :else (util/input (str \newline const/continue-prompt)))
    game-with-stock-exchange))

(defn get-player-move []
  (util/input (str \newline "What is your move? ")))

(defn tally-scores [game-data]
  (util/display (str \newline
                     "Tallying scores ..." \newline))
  ; XXX get top-score
  ; XXX determine tie-breaking, if necessary
  ; XXX display "scoreboard"
  )

(defn parse-command
  "Some of the command functions will return new game-data (e.g., 'load-game');
  all the rest should return 'nil'."
  [game-data command]
  (cond
    (util/in? (game-command/get-commands "commands") command)
      (game-command/display-commands)
    (util/in? (game-command/get-commands "help") command)
      (game-command/display-help)
    (util/in? (game-command/get-commands "load") command)
      (game-command/load-game)
    (util/in? (game-command/get-commands "map") command)
      nil
    (util/in? (game-command/get-commands "order") command)
      (game-command/display-player-order game-data)
    (or (util/in? (game-command/get-commands "quit") command)
        (util/in? (game-command/get-commands "exit") command))
      (game-command/quit-game tally-scores game-data)
    (util/in? (game-command/get-commands "restart") command)
      (setup-game)
    (util/in? (game-command/get-commands "save") command)
      (game-command/save-game game-data)
    (util/in? (game-command/get-commands "score") command)
      (game-command/display-score game-data)
    (util/in? (game-command/get-commands "stock") command)
      (finance/display-stock game-data)))

(defn process-command
  "For command functions that return 'nil', simply run 'do-player-turn' again
  with the same moves. If a command function does not return 'nil', it's
  likely returning new game-data, and moves should not be included, as they
  will be recalculated. This is the case when loading game data from a file."
  [game-data available-moves command]
  (let [post-parse-game-data (parse-command game-data command)]
    (cond
      (nil? post-parse-game-data)
        (do-player-turn
          game-data
          available-moves)
      :else
        (do-player-turn
          post-parse-game-data))))

(defn process-move [game-data move]
  (do-player-turn
    (game-move/inc-move
      (game-move/update-map-with-move move game-data))))

(defn play-again? []
  (let [response (util/input
                   (str \newline "Would you like to play again? [Y/n] "))]
    (cond
      (.startsWith (string/lower-case response) "n") false
      :else true)))

(defn do-endgame [game-data]
  ; display last version of map
  (layout/draw-grid game-data)
  (tally-scores game-data)
  (cond
    (play-again?) (do-player-turn (setup-game))
    :else (util/exit)))

(defn -display-map-and-moves [game-data available-moves]
  (layout/draw-grid game-data)
  (game-command/display-moves available-moves game-data))

(defn display-map-and-moves [game-data available-moves]
  (cond
    (game-move/moves-remain? game-data)
      (-display-map-and-moves game-data available-moves)
    :else (do-endgame game-data)))

(defn do-bad-input [game-data available-moves input]
  (util/display (str \newline "Whoops! Your input of '" input
                     "' was not understood. Please try again."
                     \newline \newline))
  (util/input const/continue-prompt)
  (do-player-turn
    game-data
    available-moves))

(defn validate-move [game-data available-moves move]
  (cond
    (game-move/legal? available-moves move)
      (process-move game-data move)
    (game-command/legal? move)
      (process-command game-data available-moves move)
    :else (do-bad-input game-data available-moves move)))

(defn do-player-turn
  ([game-data]
    (do-player-turn
      game-data
      (game-move/get-friendly-moves game-data)))
  ([game-data available-moves]
    (display-map-and-moves game-data available-moves)
    (validate-move
      game-data
      available-moves
      (string/lower-case
        (get-player-move)))))
