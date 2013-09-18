(ns starlanes.game.movement-test
  (:require [clojure.test :refer :all]
            [starlanes.game.movement :as game]
            [starlanes.util :as util]))


(deftest test-get-remainin-moves
  (is (= 22 (game/get-remaining-moves util/fake-game-data))))

(deftest test-get-total-poassible-move-count
  ; test with turns enabled
  (let [win-by-turns true]
    (let [remaining-moves 22
          move-count 5
          game-data util/fake-game-data]
      (is (= 5 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with a different max move count
    (let [remaining-moves 22
          move-count 4
          game-data util/fake-game-data]
      (is (= 4 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with possible moves minimized
    (let [remaining-moves 22
          move-count 0
          game-data util/fake-game-data]
      (is (= 0 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))))

(deftest test-get-total-poassible-move-count-no-win-by-turns
  ; test with win-by-turns disabled
  (let [win-by-turns false]
    (let [remaining-moves 22
          move-count 5
          game-data util/fake-game-data]
      (is (= 22 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with a different number of remaining moves
    (let [remaining-moves 10
          move-count 5
          game-data util/fake-game-data]
      (is (= 10 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with a different number of remaining moves
    (let [remaining-moves 4
          move-count 5
          game-data util/fake-game-data]
      (is (= 4 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with possible moves minimized
    (let [remaining-moves 22
          move-count 0
          game-data util/fake-game-data]
      (is (= 22 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with remaining/possible moves minimized
    (let [remaining-moves 0
          move-count 0
          game-data util/fake-game-data]
      (is (= 0 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))

    ; test with remaining moves minimized
    (let [remaining-moves 0
          move-count 5
          game-data util/fake-game-data]
      (is (= 0 (game/get-total-possible-move-count
                  win-by-turns remaining-moves move-count game-data))))))

(deftest test-get-remaining-moves-for-turn
  (let [max-moves-per-turn 2
        current-move-index 0]
    (is (= 2 (game/get-remaining-moves-for-turn
               max-moves-per-turn
               current-move-index))))

  (let [max-moves-per-turn 0
        current-move-index 0]
    (is (= 0 (game/get-remaining-moves-for-turn
               max-moves-per-turn
               current-move-index))))

  (let [max-moves-per-turn 5
        current-move-index 0]
    (is (= 5 (game/get-remaining-moves-for-turn
               max-moves-per-turn
               current-move-index))))

  (let [max-moves-per-turn 5
        current-move-index 4]
    (is (= 1 (game/get-remaining-moves-for-turn
               max-moves-per-turn
               current-move-index))))

  (let [max-moves-per-turn 10
        current-move-index 2]
    (is (= 8 (game/get-remaining-moves-for-turn
               max-moves-per-turn
               current-move-index)))))

(deftest test-get-remaining-moves-for-game
  (is (= 0 (game/get-remaining-moves-for-game true 3 3 10))))

(deftest test-moves-remain-many
  (is (= 6 (game/-moves-remain? util/fake-game-data))))

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

