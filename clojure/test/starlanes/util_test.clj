(ns starlanes.util-test
  (:require [clojure.test :refer :all]
            [starlanes.const :as const]
            [starlanes.game.base :as game]
            [starlanes.util :as util]))


(deftest test-mult-str
  (let [result (util/mult-str 4 "ab")
        expected "abababab"]
    (is (= result expected))))

(deftest test-ord
  (is (= 97 (util/ord "a")))
  (is (= 122 (util/ord "z"))))

(deftest test-chr
  (is (= "a" (util/chr 97)))
  (is (= "z" (util/chr 122))))

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

(deftest test-coord-open?
  (is (= true (util/coord-open? [:a1 "."] ".")))
  (is (= false (util/coord-open? [:a1 "."] "*")))
  (is (= false (util/coord-open? [:a1 "."] "+")))
  (is (= false (util/coord-open? [:a1 "."] "A"))))

(deftest test-string->xy
  (is (= ["a" "1"] (util/string->xy "a1")))
  (is (= ["b" "12"] (util/string->xy "b12")))
  (is (= ["c" "123"] (util/string->xy "c123"))))

(deftest test-keyword->xy
  (is (= ["a" "1"] (util/keyword->xy :a1)))
  (is (= ["b" "12"] (util/keyword->xy :b12)))
  (is (= ["c" "123"] (util/keyword->xy :c123))))

(deftest test-xy->keyword
  (is (= :a1 (util/xy->keyword ["a" 1])))
  (is (= :b12 (util/xy->keyword ["b" 12])))
  (is (= :c123 (util/xy->keyword ["c" "123"]))))

(deftest test-move->string-coord
  (is (= "a1" (util/move->string-coord "1a")))
  (is (= "a12" (util/move->string-coord "12a")))
  (is (= "a123" (util/move->string-coord "123a"))))

(deftest test-move->keyword
  (is (= :a1 (util/move->keyword "1a")))
  (is (= :a12 (util/move->keyword "12a")))
  (is (= :a123 (util/move->keyword "123a"))))

(deftest test-get-friendly-coord
  (is (= "1a" (util/get-friendly-coord :a1)))
  (is (= "12b" (util/get-friendly-coord :b12)))
  (is (= "123c" (util/get-friendly-coord :c123))))

(deftest test-is-item?
  (is (= true (util/is-item? [:a1 "*"] (const/items :star))))
  (is (= false (util/is-item? [:a1 "."] (const/items :star))))
  (is (= true (util/is-item? [:a1 "+"] (const/items :outpost))))
  (is (= true (util/is-item? [:a1 "."] (const/items :empty)))))

(deftest test-filter-item
  (is (= [:a1 "*"] (util/filter-item [:a1 "*"] (const/items :star))))
  (is (= [:a1 "+"] (util/filter-item [:a1 "+"] (const/items :outpost))))
  (is (= nil (util/filter-item [:a1 "+"] (const/items :star))))
  (is (= [nil nil nil nil nil nil nil nil nil
          [:c3 "*"] nil nil nil nil [:e4 "*"]
          nil nil nil nil nil nil nil [:a1 "*"]
          nil nil]
         (map #(util/filter-item % (const/items :star))
         (util/fake-game-data :star-map)))))

(deftest test-filter-allowed
  (is (= #{2 3 4} (util/filter-allowed [1 2 3 4 5] [2 3 4])))
  (is (= #{[:a2 "*"]} (util/filter-allowed [[:a1 "."] [:a2 "*"]] [[:a2 "*"]]))))

(deftest test-get-x-coord-range
  (is (= ["a" "b" "c" "d" "e"] (util/get-x-coord-range))))

(deftest test-get-y-coord-range
  (is (= [1 2 3 4 5] (util/get-y-coord-range))))

(deftest test-in?
  (is (= true (util/in? [1 2] 1)))
  (is (= true (util/in? [1 2] 2)))
  (is (= false (util/in? [1 2] 0)))
  (is (= false (util/in? [1 2] 3))))

(deftest test-x-coord?
  (is (= true (util/x-coord? "a")))
  (is (= true (util/x-coord? "e")))
  (is (= false (util/x-coord? "f"))))

(deftest test-y-coord?
  (is (= false (util/y-coord? 0)))
  (is (= true (util/y-coord? 1)))
  (is (= true (util/y-coord? 5)))
  (is (= false (util/y-coord? 6))))

(deftest test-serialize-game-data
  (is (= {:rand nil} (util/serialize-game-data {:rand "hey!"}))))

(deftest test-get-players
  (is (= ["Alice" "Bob" "Carol"]
         (map #(% :name) (util/get-players util/fake-game-data)))))

(deftest test-get-players
  (is (= 3 (util/get-player-count util/fake-game-data))))

(deftest test-get-max-total-moves
  (is (= 6 (util/get-max-total-moves util/fake-game-data)))
  (is (= 9 (util/get-max-total-moves 3 3)))
  (is (= 40 (util/get-max-total-moves 10 4)))
  (is (= 15 (util/get-max-total-moves 1 15)))
  (is (= 1 (util/get-max-total-moves 1 1)))
  (is (= 0 (util/get-max-total-moves 0 0))))



