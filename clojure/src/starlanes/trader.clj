; Star Traders
; MODIFIED FOR 'ALTAIR BASIC 4.0' BY - S J SINGER
; MODIFIED FOR THE MICROBEE BY JOHN ZAITSEFF, 1988
; Ported to Clojure by Duncan McGreggor, 2013
(ns starlanes.trader
  (:require [starlanes.game.base :as game]
            [starlanes.instructions :as instructions]
            [starlanes.util :as util])
  (:gen-class))


(defn -main []
  (let [game-data (game/setup-game)]
    (game/do-player-turn game-data)))