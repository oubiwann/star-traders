(ns starlanes.finance-test
  (:require [clojure.test :refer :all]
            [starlanes.finance :as finance]
            [starlanes.util :as util]))


(deftest test-compute-value
  (let [assets [1000 [{:stock 12 :value 23.50} {:stock 100 :value 50}]]]
    (is (= 6282.0 (finance/compute-value assets)))
    (is (= 6282.0 (finance/compute-value (first assets) (second assets))))))


