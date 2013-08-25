; Star Traders
; MODIFIED FOR 'ALTAIR BASIC 4.0' BY - S J SINGER
; MODIFIED FOR THE MICROBEE BY JOHN ZAITSEFF, 1988
; Ported to Clojure by Duncan McGreggor, 2013
(ns starlanes.trader
  (:require [starlanes.game :as game]
            [starlanes.layout :as layout]
            [starlanes.util :as util])
  (:gen-class))


(defn -main []
  (util/clear-screen)
  (def game-data (game/game-data-factory))
  ;(player.set-players game-data)
  ;(player.determine-player-order game-data)
  ;(instructions.check)
  (layout/draw-new-grid game-data)
  (let [data (util/input "What is your answer? ")]
    (util/display data))
  ;(setv command (game.process-next-move game-data))
  ;(while (!= command "quit")
  ;  (layout.redraw-grid game-data)
  ;  (if (game.max-turn? game-data)
  ;    (setv command (game.game-over "Maximum turn limit hit!"))
  ;    (setv command (game.process-next-move game-data)))))
  )