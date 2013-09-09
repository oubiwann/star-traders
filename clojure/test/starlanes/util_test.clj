(ns starlanes.util-test
  (:require [clojure.test :refer :all]
            [starlanes.const :as const]
            [starlanes.game :as game]
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

(deftest test-coord-open?
  (is (= true (util/coord-open? [:a1 "."] ".")))
  (is (= false (util/coord-open? [:a1 "."] "*")))
  (is (= false (util/coord-open? [:a1 "."] "+")))
  (is (= false (util/coord-open? [:a1 "."] "A"))))

(deftest test-keyword->xy
  (is (= ["a" "1"] (util/keyword->xy :a1)))
  (is (= ["b" "12"] (util/keyword->xy :b12)))
  (is (= ["c" "123"] (util/keyword->xy :c123))))

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
  (is (= [nil nil nil nil nil nil nil nil nil [:c3 "*"]
          nil nil nil nil nil nil nil nil nil nil nil nil nil nil nil]
         (map #(util/filter-item % (const/items :star))
         (util/fake-game-data :star-map)))))

(deftest test-filter-allowed
  (is (= #{2 3 4} (util/filter-allowed [1 2 3 4 5] [2 3 4])))
  (is (= #{[:a2 "*"]} (util/filter-allowed [[:a1 "."] [:a2 "*"]] [[:a2 "*"]]))))

(deftest test-get-valid-coord-range
  (is (= [] (util/get-valid-coord-range [:a1] 97 104)))
  )

