(ns starlanes.game.command-test
  (:require [clojure.test :refer :all]
            [starlanes.game.command :as game]
            [starlanes.util :as util]))


(deftest test-get-commands
  (is (= [["stock" "s"] ["map" "m"] ["order" "o"] ["score"] ["save"] ["load"]
          ["commands" "c"] ["help" "h"] ["restart"] ["quit" "q"] ["exit" "x"]]
         (util/get-commands)))
  (is (= ["stock" "s"] (util/get-commands "stock")))
  (is (= ["load"] (util/get-commands "load"))))