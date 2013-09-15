(ns starlanes.game.base-test
  (:require [clojure.test :refer :all]
            [starlanes.game.base :as game]
            [starlanes.util :as util]))


(deftest test-game-data-factory
  (let [game-data (game/game-data-factory)]
    (is (= (game-data :star-map) {}))
    (is (= (game-data :total-moves) 0))
    (is (= (game-data :companies) []))
    (is (= (str (util/rand-float (game-data :rand)))
           (str 0.75192976)))))


