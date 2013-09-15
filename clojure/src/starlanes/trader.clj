; Star Traders
; MODIFIED FOR 'ALTAIR BASIC 4.0' BY - S J SINGER
; MODIFIED FOR THE MICROBEE BY JOHN ZAITSEFF, 1988
; Ported to Clojure by Duncan McGreggor, 2013
(ns starlanes.trader
  (:require [starlanes.game.base :as game]
            [starlanes.instructions :as instructions]
            [starlanes.layout :as layout]
            [starlanes.player :as player]
            [starlanes.util :as util])
  (:gen-class))


(defn setup-game []
  (let [game-init (game/game-data-factory)
        game-with-star-map (game/create-star-map-for-game game-init)
        game-with-players (game/set-new-players game-with-star-map)
        game-with-player-order (player/determine-player-order game-with-players)
        ]
    game-with-player-order))

(defn -main []
  (util/clear-screen)
  (let [game-data (setup-game)]
    (instructions/display?)
    (game/do-player-turn game-data)
    ;(setv command (game.process-next-move game-data))
    ;(while (!= command "quit")
    ;  (layout.redraw-grid game-data)
    ;  (if (game.max-turn? game-data)
    ;    (setv command (game.game-over "Maximum turn limit hit!"))
    ;    (setv command (game.process-next-move game-data)))))
   ))