(ns starlanes.util-test
  (:use clojure.test)
  (:require [starlanes.game :as game]
            [starlanes.util :as util]))

(deftest test-mult-str
  (let [result (util/mult-str 4 "ab")
        expected "abababab"]
    (is (= result expected))))

(deftest test-mult-str
  (let [result (util/mult-str 4 "ab")
        expected "abababab"]
    (is (= result expected))))

(deftest test-rand-float
  (let [random (util/random 314)
        result (util/rand-float random)
        expected 0.75192976]
    (is (= (str result) (str expected)))))

(deftest test-rand-game
  (let [game-data (game/game-data-factory)
        result (util/rand-game game-data)
        expected 0.75192976]
    (is (= (str result) (str expected)))))