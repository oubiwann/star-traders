; Star Traders
; MODIFIED FOR 'ALTAIR BASIC 4.0' BY - S J SINGER
; MODIFIED FOR THE MICROBEE BY JOHN ZAITSEFF, 1988
; Ported to Hy by Duncan McGreggor, 2013
(import random)

(import (starlanes (config game instructions layout player util)))


(defun main ()
  (util.clear-screen)
  (setv game-data (game.Game-Data))
  (player.set-players game-data)
  (random.seed config.seed)
  (player.determine-player-order game-data)
  (instructions.check)
  (layout.draw-new-grid game-data)
  (setv command (game.process-next-move game-data))
  (while (!= command "quit")
    (layout.redraw-grid game-data)
    (if (game.max-turn? game-data)
      (setv command (game.game-over "Maximum turn limit hit!"))
      (setv command (game.process-next-move game-data)))))

(main)
