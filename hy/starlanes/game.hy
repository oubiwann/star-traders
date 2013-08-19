(import random)

(import (starlanes (config finance player util)))


(defclass Game-Data (object)
  ((star-map {})
   (total-moves 0)
   (players [])
   (player-order [])
   (move None)
   (companies [])))

(defun display-map ()
  (import (starlanes (layout)))
  ; XXX pass moves here, so that new moves aren't generated whenever a player
  ; displays the map again
  (layout.redraw-grid))

(defun display-stock (current-player)
  (import (starlanes (finance)))
  ; do something
  )

(defun update-coords (x y data game-data)
  (.update game-data.star-map {(, (config.make-y-coord y) x) data}))

(defun get-map-items (game-data)
  (sorted (game-data.star-map.items)))

(defun get-open-coords (game-data)
  (list-comp
    (car data)
    (data (get-map-items game-data))
    (= (car (cdr data)) config.grid-point)))

(defun get-move-count (game-data)
  (if (>= (len (get-open-coords game-data)) config.max-moves)
    config.max-moves
    (len (get-open-coords))))

(defun get-moves (game-data)
  (setv choices (set))
  (setv move-count (get-move-count game-data))
  (while (!= move-count (len choices))
    (choices.add
      (random.choice
        (get-open-coords game-data)))
    (setv move-count (get-move-count game-data)))
  choices)

(defun get-friendly-moves (moves)
  (list-comp
    (+
      (.join config.space (slice x 0 -1))
      (get x -1))
    (x moves)))

(defun print-moves (game-data &optional moves)
  (if (= moves None)
    (setv moves (get-moves game-data)))
  (print
    (+ "  "
      (.join config.space
        (get-friendly-moves moves)))))

(defun get-item-coords (item-char game-data)
  (for ((, coord item) (game-data.star-map.items))
    (if (= item item-char)
      (yield coord))))

(defun get-star-coords (game-data)
  (get-item-coords config.star-char game-data))

(defun get-outpost-coords (game-data)
  (get-item-coords config.outpost-char game-data))

(defun -get-company-coords (game-data)
  (setv company-chars (kwapply (finance.get-companies) {"initials" true}))
  (for (company-char company-chars)
    (yield (get-item-coords company-char game-data))))

(defun get-company-coords (game-data)
  (apply (itertools.chain) (-get-company-coords game-data)))

(defun -get-possible-coords (coord filter)
  (filter
    [(- coord 1) coord (+ coord 1)]))

(defun -get-possible-x-coords (coord)
  (list-comp
    (chr filtered-x)
    (filtered-x
      (-get-possible-coords
        coord
        util.get-valid-x-coords))))

(defun -get-possible-y-coords (coord)
  (list-comp
    filtered-y
    (filtered-y
      (-get-possible-coords
        coord
        util.get-valid-y-coords))))

(defun get-neighbors (coord)
  (setv x-coord (ord (car coord)))
  (setv y-coord (car (cdr coord)))
  (setv xs (-get-possible-x-coords x-coord))
  (setv ys (-get-possible-y-coords y-coord))
  (for (x xs)
    (for (y ys)
      (if (!= [x y] coord)
        (yield (, (str y) x))))))

(defun -near-item? (neighbors all-item-coords)
  (setv item-neighbors (neighbors.intersection (set all-item-coords)))
  (if item-neighbors
    true
    false))

(defun next-to-star? (neighbors game-data)
  (-near-item? neighbors (get-star-coords game-data)))

(defun next-to-outpost? (neighbors game-data)
  (-near-item? neighbors (get-outpost-coords game-data)))

(defun get-move-char (coord game-data)
  (setv neighbors (set (get-neighbors coord)))
  (setv coord (, (str (get coord 1)) (get coord 0)))
  (cond
    ((next-to-star? neighbors game-data)
      (print "next to a star!"))
    ((next-to-outpost? neighbors game-data)
      (print "next to an outpost!")))
  config.outpost-char)

(defun -process-illegal-move (moves game-data)
  (print "That space was not included in the list ...")
  (-process-next-move moves game-data))

(defun -process-legal-move (move-choice game-data)
  (setv coord (util.move->coords move-choice))
  (setv move-char (get-move-char coord game-data))
  (update-coords (get coord 0)
                 (get coord 1)
                 move-char
                 game-data)
  (game-data.move.tick))

(defun -process-next-move (moves game-data)
  (setv current-player (player.get-current-player game-data))
  (setv msg (+ current-player.name ", "))
  (print (+ "\n"
            current-player.name
            ", here are your legal moves for this turn:"))
  (print-moves game-data moves)
  (setv move-choice (.lower (raw-input "\nWhat is your move? ")))
  (cond
    ((in move-choice ["quit" "q"])
      "quit")
    ((= move-choice "map")
      ; XXX currently this also gives new random moves/choices; choices should
      ; remain the same after redraws and only change when it's the next
      ; player's turn
      (display-map))
    ((= move-choice "stock")
      (display-stock current-player))
    ((not (in move-choice (get-friendly-moves moves)))
      (-process-illegal-move moves game-data))
    (true
      (-process-legal-move move-choice game-data))))

(defun game-over (reason)
  (print (+ "\nGAME OVER: " reason "\n"))
  "quit")

(defun process-next-move (game-data)
  (setv moves (get-moves game-data))
  (if moves
    (-process-next-move moves game-data)
    (game-over "No more moves!")))

(defun get-turn-count (game-data)
  (setv turn game-data.move.parent)
  (if (= turn None)
    0
    game-data.move.parent.count))

(defun max-turn? (game-data)
  (if config.win-by-turns
    (if (>= (get-turn-count game-data) config.max-turns)
      true)))
