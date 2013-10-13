(ns starlanes.finance-test
  (:require [clojure.test :refer :all]
            [starlanes.finance :as finance]
            [starlanes.util :as util]))


(deftest test-exchange-factory
  (is (= [:A] (sort (keys (finance/exchange-factory ["A"])))))
  (is (= [:A :B] (sort (keys (finance/exchange-factory ["A" "B"])))))
  (is (= [:A :B :C] (sort (keys (finance/exchange-factory ["A" "B" "C"])))))
  (is (= [:Alice :Bob]
         (sort (keys ((finance/exchange-factory ["A"] ["Alice" "Bob"]) :A)))))
  (is (= [:Alice :Bob :Carol]
         (sort
          (keys
            ((finance/exchange-factory
              ["A" "B"]
              ["Alice" "Bob" "Carol"]) :B)))))
  (is (= [:Alice :Bob :Carol :Dave]
         (sort
          (keys
            ((finance/exchange-factory
              ["A" "B" "C"]
              ["Alice" "Bob" "Carol" "Dave"]) :C))))))

(deftest test-get-company-holdings
  (is (= [:Alice :Bob :Carol]
         (sort
           (keys
             (finance/get-company-holdings "A" util/fake-finance-data)))))
  (is (= [:Carol]
         (sort
           (keys
             (finance/get-company-holdings "B" util/fake-finance-data)))))
  (is (= [:Bob :Carol]
         (sort
           (keys
             (finance/get-company-holdings "C" util/fake-finance-data))))))

(deftest test-get-player-shares
  (is (= 1000 (finance/get-player-shares "A" "Alice" util/fake-finance-data)))
  (is (= 500 (finance/get-player-shares "A" "Bob" util/fake-finance-data)))
  (is (= 100 (finance/get-player-shares "A" "Carol" util/fake-finance-data)))
  (is (= 0 (finance/get-player-shares "B" "Alice" util/fake-finance-data)))
  (is (= 1000 (finance/get-player-shares "B" "Carol" util/fake-finance-data)))
  (is (= 500 (finance/get-player-shares "C" "Bob" util/fake-finance-data)))
  (is (= 100 (finance/get-player-shares "C" "Carol" util/fake-finance-data)))
  (is (= 0 (finance/get-player-shares "Z" "Bob" util/fake-finance-data))))

(deftest test-get-new-holdings
  )

(deftest test-get-new-company
  (let [result (finance/get-new-company)]
    (is (= (result :name) ""))
    (is (= (result :units) 0))
    (is (= (result :share-mod) 0.0)))
  (let [result (finance/get-new-company "A" 2 25.00)]
    (is (= (result :name) "A"))
    (is (= (result :units) 2))
    (is (= (result :share-mod) 25.00))))

(deftest compute-stock-value
  )

(deftest compute-stocks-value
  )

(deftest test-compute-value
  (let [assets [1000 [{:stock 12 :value 23.50} {:stock 100 :value 50}]]]
    (is (= 6282.0 (finance/compute-value assets)))
    (is (= 6282.0 (finance/compute-value (first assets) (second assets))))))

(deftest test-get-next-company
  )

(deftest test-add-company
  (is (= [] (util/fake-game-data :companies)))
  (let [[company-name game-data] (finance/add-company
                                   2 55.00 util/fake-game-data)]
    (is (= [{:share-mod 55.0 :units 2 :name "Al"}] (game-data :companies)))
    (is (= ["Be" "Ca" "De" "Er"] (game-data :companies-queue)))
    (is (= "Al" company-name))
    (let [[company-name game-data] (finance/add-company 3 22.00 game-data)]
      (is (= [{:share-mod 55.0 :units 2 :name "Al"}
              {:share-mod 22.0 :units 3 :name "Be"}] (game-data :companies)))
      (is (= ["Ca" "De" "Er"] (game-data :companies-queue)))
      (is (= "Be" company-name)))))

(deftest test-filter-company
  (let [companies [{:name "A" :units 1 :share-mod 0.01}
                   {:name "B" :units 2 :share-mod 10}]]
    (is (= [{:share-mod 0.01 :units 1 :name "A"}
            {:share-mod 10 :units 2 :name "B"}]
           (finance/filter-company "a" companies)))
    (is (= [{:share-mod 10 :units 2 :name "B"}]
           (finance/filter-company "A" companies)))))

(deftest test-remove-company
  (let [game-data {:companies [{:share-mod 55.0 :units 3 :name "Al"}
                               {:share-mod 22.0 :units 4 :name "Be"}]
                   :companies-queue ["Ca" "De" "Er"]}
        game-data (finance/remove-company "Al" game-data)]
    (is (= [{:share-mod 22.0 :units 4 :name "Be"}] (game-data :companies)))
    (is (= ["Al" "Ca" "De" "Er"] (game-data :companies-queue)))))

(deftest test-get-players-shares
  )

(deftest test-get-companies-base-counts
  (let [counts (finance/get-companies-base-counts util/fake-game-data)]
    (is (= {"A" 3, "C" 1, "B" 2} counts))
    (is (= 3 (counts "A")))
    (is (= 2 (counts "B")))
    (is (= 1 (counts "C")))
    (is (= nil (counts "Z")))))

(deftest test-get-company-base-count
    (is (= 3 (finance/get-company-base-count "A" util/fake-game-data)))
    (is (= 2 (finance/get-company-base-count "B" util/fake-game-data)))
    (is (= 1 (finance/get-company-base-count "C" util/fake-game-data)))
    (is (= 0 (finance/get-company-base-count "Z" util/fake-game-data))))

(deftest test-get-company-star-count
  (is (= 3 (finance/get-company-star-count "A" util/fake-game-data)))
  (is (= 1 (finance/get-company-star-count "B" util/fake-game-data)))
  (is (= 0 (finance/get-company-star-count "C" util/fake-game-data)))
  (is (= 0 (finance/get-company-star-count "Z" util/fake-game-data))))

(deftest test-get-companies-star-counts
  (is (= {"A" 3, "B" 1}
         (finance/get-companies-star-counts util/fake-game-data))))

(deftest test-get-share-value
  (is (= 1800 (finance/get-share-value "A" util/fake-game-data)))
  (is (= 700 (finance/get-share-value "B" util/fake-game-data)))
  (is (= 100 (finance/get-share-value "C" util/fake-game-data)))
  )

(deftest test-get-company-value
  )

(deftest test-get-companies-values
  )

(deftest test-get-filtered-companies
  (is (= [[:a5 "C"] [:d1 "A"] [:e1 "A"] [:e2 "A"]]
         (finance/get-filtered-companies ["A" "C"] util/fake-game-data)))
  (is (= [[:a3 "B"] [:a5 "C"] [:b3 "B"]]
         (finance/get-filtered-companies ["B" "C"] util/fake-game-data)))
  (is (= [[:a3 "B"] [:b3 "B"]]
         (finance/get-filtered-companies ["B"] util/fake-game-data))))


