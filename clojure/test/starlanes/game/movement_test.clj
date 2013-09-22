(ns starlanes.game.movement-test
  (:require [clojure.test :refer :all]
            [starlanes.game.movement :as game]
            [starlanes.util :as util]))


(deftest test-get-remaining-moves
  (is (= 21 (game/get-remaining-moves util/fake-game-data))))

(deftest test-moves-remain-many
  (is (= 21 (game/-moves-remain? util/fake-game-data))))

(deftest test-moves-remain-some
  (let [max-moves 10
        moves-so-far 4
        remaining 4]
    (let [win-by-turns false]
      (is (= 4 (game/-moves-remain?
                 win-by-turns max-moves moves-so-far remaining))))
    (let [win-by-turns true]
      (is (= 6 (game/-moves-remain?
                 win-by-turns max-moves moves-so-far remaining))))))

(deftest test-moves-remain-few
  (let [max-moves 10
        moves-so-far 8
        remaining 2]
    (let [win-by-turns false]
      (is (= 2 (game/-moves-remain?
                  win-by-turns max-moves moves-so-far remaining))))
    (let [win-by-turns true]
      (is (= 2 (game/-moves-remain?
                  win-by-turns max-moves moves-so-far remaining))))))

(deftest test-moves-remain-none
  (let [max-moves 6
        moves-so-far 6
        remaining 32]
    (let [win-by-turns false]
      (is (= 32 (game/-moves-remain?
                  win-by-turns max-moves moves-so-far remaining)))
      (is (= 0 (game/-moves-remain?
                  win-by-turns max-moves moves-so-far 0))))
    (let [win-by-turns true]
      (is (= 0 (game/-moves-remain?
                  win-by-turns max-moves moves-so-far remaining))))))

