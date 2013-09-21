(ns starlanes.finance-test
  (:require [clojure.test :refer :all]
            [starlanes.finance :as finance]
            [starlanes.util :as util]))


(deftest compute-stock-value
  )

(deftest compute-stocks-value
  )

(deftest test-compute-value
  (let [assets [1000 [{:stock 12 :value 23.50} {:stock 100 :value 50}]]]
    (is (= 6282.0 (finance/compute-value assets)))
    (is (= 6282.0 (finance/compute-value (first assets) (second assets))))))

(deftest test-get-companies
  (is (= ["Altair Starways"
          "Betelgeuse, Ltd."
          "Capella Cargo Co."
          "Denebola Shippers"
          "Eridani Expediters"]
         (finance/get-companies))))

(deftest test-get-next-company
  )

(deftest test-create-new-company
  (let [result (finance/create-new-company "A" 2 25.00)]
    (is (= (result :name) "A"))
    (is (= (result :units) 2))
    (is (= (result :share-mod) 25.00))))

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

(deftest test-update-player-shares
  )

(deftest test-update-company-share-value
  )

(deftest test-perform-company-merger
  )
