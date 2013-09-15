(ns starlanes.game
  (:require [clojure.math.combinatorics :as combi]
            [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.finance :as finance]
            [starlanes.layout :as layout]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(declare do-player-turn)

(defn game-data-factory []
  {:star-map (sorted-map),
   :total-moves 0,
   :players [],
   :player-order [],
   :move 0,
   :companies [],
   :share-value {},
   :rand (util/random const/seed)})

(defn create-item [rand-float]
  (cond
    (<= rand-float const/star-rate)
    (const/items :star)
    :else (const/items :empty)))

(defn create-game-item [game-data]
  (create-item
    (util/rand-game game-data)))

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
      (map
        -combine-coords
        star-map-data))))

(defn create-star-map-for-game [game-data]
  (let [star-map (create-star-map game-data)]
    (conj game-data {:star-map star-map})))

(defn set-new-players
  ([]
    (set-new-players (game-data-factory)))
  ([game-data]
    (conj game-data {:players (doall (player/get-new-players))})))

(defn update-coords
  "Return a new game-data data structure with a new item at the given
  coordinates."
  [move coord-item game-data]
  (let [keyword-coord (util/move->keyword move)
        old-star-map (game-data :star-map)
        new-star-map (conj old-star-map [keyword-coord coord-item])]
    (conj game-data {:star-map new-star-map})))

(defn inc-move
  "Return a new game-data data structure with the total move count incremented."
  [game-data]
  (conj
    game-data
    {:total-moves (inc (game-data :total-moves))}))

(defn get-empty-coord [coord-data]
  (if (util/coord-open? coord-data (const/items :empty))
    (first coord-data)))

(defn get-open-coords [game-data]
  (remove
    nil?
    (map
      get-empty-coord
      (game-data :star-map))))

(defn get-total-possible-move-count [game-data]
  (let [open-coords (count (get-open-coords game-data))]
    (cond
      const/win-by-turns?
      (cond
        (>= open-coords const/max-moves)
        const/max-moves
        :else open-coords)
      :else open-coords)))

(defn get-moves [game-data]
  (let [open-coords (get-open-coords game-data)]
    (take const/max-moves (shuffle open-coords))))

(defn get-friendly-moves [game-data]
  (map
    util/get-friendly-coord
    (get-moves game-data)))

(defn print-moves [moves game-data]
  (util/display
    (str
      \newline
      ((player/get-current-player game-data) :name)
      ", here are your legal moves for this turn:"
      \newline
      "  "
      (string/join
        \space moves)
      \newline)))

(defn get-item-coords [item-char game-data]
  (remove
    nil?
    (map
      #(first (util/filter-item % item-char))
      (game-data :star-map))))

(defn get-star-coords [game-data]
  (get-item-coords (const/items :star) game-data))

(defn get-outpost-coords [game-data]
  (get-item-coords (const/items :outpost) game-data))

(defn get-possible-neighbors [coord]
    [(dec coord) coord (inc coord)])

(defn get-possible-x-neighbors
  "This function takes a letter representing the x component of a coordinate
  pair and returns the legal neighbors."
  [x-coord]
  (filter
    util/x-coord?
    (map
      util/chr
      (get-possible-neighbors
        (util/ord x-coord)))))

(defn get-possible-y-neighbors
  "This function takes a number representing the y component of a coordinate
  pair and returns the legal neighbors."
  [y-coord]
  (filter
    util/y-coord?
    (get-possible-neighbors (Integer. y-coord))))

(defn get-neighbors-pairs
  "This function takes a keyword that represents a coordinate (e.g., :b23) and
  returns coordinates (pair-wise, e.g., [(x1 y1) (x2 y2) ...]) for the
  neighboring positions. The number of returned neighbors could range anywhere
  from 3 (when the given coorindate is in a corner of the map) to 8 (when the
  given coordinate is in the center of the map).
  "
  [keyword-coord]
  (let [[x-coord y-coord] (util/keyword->xy keyword-coord)
        x-neighbors (get-possible-x-neighbors x-coord)
        y-neighbors (get-possible-y-neighbors y-coord)
        pairs (combi/cartesian-product x-neighbors y-neighbors)]
    (remove #{[x-coord (Integer. y-coord)]} pairs)))

(defn get-neighbors
  "This function takes a keyword that represents a coordinate (e.g., :b23) and
  returns coordinates (as a list of keywords, e.g., [:x1y1 :x2y2 ...]) for the
  neighboring positions. The number of returned neighbors could range anywhere
  from 3 (when the given coorindate is in a corner of the map) to 8 (when the
  given coordinate is in the center of the map).
  "
  [keyword-coord]
  (map
    (comp keyword string/join)
    (get-neighbors-pairs keyword-coord)))

(defn near-item? [keyword-coord coords-for-items]
  (let [items-neighbors (flatten (map get-neighbors coords-for-items))]
    (util/in? items-neighbors keyword-coord)))

(defn next-to-star? [keyword-coord game-data]
  (near-item? keyword-coord (get-star-coords game-data)))

(defn next-to-outpost? [keyword-coord game-data]
  (near-item? keyword-coord (get-outpost-coords game-data)))

(defn next-to-company? [keyword-coord game-data]
  )

(defn get-player-move []
  (util/input (str \newline "What is your move? ")))

(defn do-bad-input [game-data available-moves input]
  (util/display (str \newline "Whoops! Your input of '" input
                     "' was not understood. Please try again."
                     \newline \newline))
  (util/input const/continue-prompt)
  (do-player-turn game-data available-moves))

(defn get-character-for-move [game-data move]
  "+")

(defn process-move [game-data move]
  (let [item-char (get-character-for-move game-data move)]
    (do-player-turn
      (inc-move
        (update-coords move item-char game-data)))))

(defn save-game [game-data]
  (util/display (str \newline
                     "Saving game ..." \newline \newline))
  ; XXX actually implement this!
  (util/input const/continue-prompt))

(defn tally-scores [game-data]
  (util/display (str \newline
                     "Tallying scores ..." \newline))
  ; XXX get top-score
  ; XXX determine tie-breaking, if necessary
  ; XXX display "scoreboard"
  )

(defn quit-game [game-data]
  (util/display (str \newline
                     "You have asked to quit the game." \newline))
  (let [answer (util/input const/confirm-prompt)]
    (cond
      (= answer "y") (do (tally-scores game-data) (util/exit)))))

(defn process-command [game-data available-moves command]
  (cond
    (util/in? ["map" "m"] command) true
    (= command "save") (save-game game-data)
    (util/in? ["stock" "s"] command) (finance/display-stock game-data)
    (util/in? ["quit" "q" "exit" "x"] command) (quit-game game-data))
  (do-player-turn game-data available-moves))

(defn validate-move [game-data available-moves move]
  (cond
    (util/in? available-moves move)
      (process-move game-data move)
    (util/in? const/commands move)
      (process-command game-data available-moves move)
    :else (do-bad-input game-data available-moves move)))

(defn check-remaining-moves [available-moves game-data]
  ; XXX get the number of moves requires for each player to move during the
  ;     current turn
  ; XXX if that is greater than the current number of moves left, gp into
  ;     end-game mode, displaying message about the reason for the end
  #_(tally-scores game-data)
  #_(util/exit))

(defn display-map-and-moves [game-data available-moves]
  (check-remaining-moves available-moves game-data)
  (layout/draw-grid game-data)
  (print-moves available-moves game-data))

(defn do-player-turn
  ([game-data]
    (do-player-turn game-data (get-friendly-moves game-data)))
  ([game-data available-moves]
    (display-map-and-moves game-data available-moves)
    (validate-move
      game-data
      available-moves
      (string/lower-case
        (get-player-move)))))
