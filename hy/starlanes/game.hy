(import random)

(import (starlanes (config finance player util)))


(defclass Game-Data (object)
  ((total-moves 0)
   (players [])
   (player-order [])
   (move None)
   (companies [])))

(defun display-map ()
  (import (starlanes (layout)))
  (layout.redraw-grid))

(defun display-stock (current-player)
  (import (starlanes (finance)))
  ; do something
  )

(defun update-coords (x y data)
  (.update config.star-map {(, (config.make-y-coord y) x) data}))

(defun get-map-items ()
  (sorted (config.star-map.items)))

(defun get-open-coords ()
  (list-comp
    (car data)
    (data (get-map-items))
    (= (car (cdr data)) config.grid-point)))

(defun get-move-count ()
  (if (>= (len (get-open-coords)) config.max-moves)
    config.max-moves
    (len (get-open-coords))))

(defun get-moves ()
  (setv choices (set))
  (setv move-count (get-move-count))
  (while (!= move-count (len choices))
    (choices.add
      (random.choice
        (get-open-coords)))
    (setv move-count (get-move-count)))
  choices)

(defun get-friendly-moves (moves)
  (list-comp
    (+
      (.join config.space (car x))
      (car (cdr x)))
    (x moves)))

(defun print-moves (&optional moves)
  (if (= moves None)
    (setv moves (get-moves)))
  (print
    (+ "  "
      (.join config.space
        (get-friendly-moves moves)))))

(defun get-item-coords (item-char)
  (for ((, coord item) (config.star-map.items))
    (if (= item item-char)
      (yield coord))))

(defun get-star-coords ()
  (get-item-coords config.star-char))

(defun get-outpost-coords ()
  (get-item-coords config.outpost-char))

(defun -get-company-coords ()
  (for (company-char in (kwapply (finance.get-companies) {"initials" true}))
    (yield (get-item-coords company-char))))

(defun get-company-coords ()
  (apply (itertools.chain) (-get-company-coords)))

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

(defun -get-possible-neighbors (coords)
  (setv x-coord (ord (car coords)))
  (setv y-coord (car (cdr coords)))
  (setv xs (-get-possible-x-coords x-coord))
  (setv ys (-get-possible-y-coords y-coord))
  (for (x xs)
    (for (y ys)
      (if (!= [x y] coords)
        (yield (, (str y) x))))))

(defun get-neighbors (coords)
  (setv all (-get-possible-neighbors coords))
  (print (list all))
  (print config.star-map)
  (print (config.star-map.keys))
  (print (list (get-star-coords)))
  (print (list (get-outpost-coords)))
  )

(defun get-move-char (coords)
  (setv neighbors (get-neighbors coords))
  config.outpost-char)

(defun -process-illegal-move (moves game-data)
  (print "That space was not included in the list ...")
  (-process-next-move moves game-data))

(defun -process-legal-move (move-choice game-data)
  (setv coords (util.move->coords move-choice))
  (setv move-char (get-move-char coords))
  (update-coords (get coords 0)
                 (get coords 1)
                 move-char)
  (game-data.move.tick))

(defun -process-next-move (moves game-data)
  (setv current-player (player.get-current-player game-data))
  (setv msg (+ current-player.name ", "))
  (print (+ "\n"
            current-player.name
            ", here are your legal moves for this turn:"))
  (print-moves moves)
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
  (setv moves (get-moves))
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