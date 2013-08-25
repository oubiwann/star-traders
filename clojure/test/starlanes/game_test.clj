(ns starlanes.game-test
  (:use clojure.test)
  (:require [starlanes.game :as game]
            [starlanes.util :as util]))


(deftest test-game-data-factory
  (let [game-data (game/game-data-factory)]
    (is (= (game-data :star-map) {}))
    (is (= (game-data :total-moves) 0))
    (is (= (game-data :companies) []))
    (is (= (str (util/rand-float (game-data :rand)))
           (str 0.75192976)))))

(deftest test-update-coords
  (let [game-data (game/update-coords "a" "01" "+" (game/game-data-factory))]
    (is (= (game-data :star-map) {:a1 "+"}))
    (let [game-data (game/update-coords "a" "02" "E" game-data)]
      (is (= (game-data :star-map) {:a1 "+", :a2 "E"})))))

(deftest test-create-item
  (is (= (game/create-item 0.04) "*"))
  (is (= (game/create-item 0.05) "*"))
  (is (= (game/create-item 0.06) ".")))