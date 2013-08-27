(ns starlanes.player-test
  (:use clojure.test)
  (:require [starlanes.game :as game]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(deftest test-player-data-factory
  (let [player (player/player-data-factory)]
    (is (= (player :name) ""))
    (is (= (player :cash) 0.0))
    (is (= (player :stocks) nil))))