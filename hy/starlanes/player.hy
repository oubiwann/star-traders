(import random)
(import (starlanes (instructions util)))


(defclass Player-Data (object)
  ((name "")
   (cash 0.0)
   (stock None)))

(defun set-players (game-data)
  (setv player-count (int (raw-input "How many players? ")))
  (for (x (xrange player-count))
    (setv player (Player-Data))
    (setv player.name
      (raw-input (+ "Player " (str (+ x 1)) ", what is your name? ")))
    (game-data.players.append player))
    (setv game-data.move (util.Counter player-count)))

(defun set-player-order (game-data)
  (setv player-count (len game-data.players))
  (setv game-data.player-order (range player-count))
  (random.shuffle game-data.player-order))

(defun print-player-order (game-data)
  (for (x game-data.player-order)
    (setv player (get game-data.players x))
    (print (+ "\t" player.name))))

(defun get-players (game-data)
  game-data.players)

(defun get-player-count (game-data)
  (len game-data.players))

(defun get-player-names (game-data)
  (list-comp
    player.name
    (player (get-players game-data))))

(defun get-current-player (game-data)
  (setv current-player-index
    (get game-data.player-order game-data.move.count))
  (get game-data.players current-player-index))

(defun determine-player-order (game-data)
  (set-player-order game-data)
  (print "The order of play is:")
  (print-player-order game-data))
  ;(raw-input (+ "\n" instructions.prompt)))
