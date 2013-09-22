(ns starlanes.game.command-test
  (:require [clojure.test :refer :all]
            [starlanes.game.command :as game]
            [starlanes.util :as util]))


(deftest test-get-commands
  (is (= [["stock" "s"] ["map" "m"] ["order" "o"] ["score"] ["save"] ["load"]
          ["commands" "c"] ["help" "h"] ["restart"] ["quit" "q"] ["exit" "x"]]
         (game/get-commands)))
  (is (= ["stock" "s"] (game/get-commands "stock")))
  (is (= ["load"] (game/get-commands "load"))))

(deftest test-get-legal-commands
  (is (= ["stock" "s" "map" "m" "order" "o" "score" "save" "load" "commands" "c"
          "help" "h" "restart" "quit" "q" "exit" "x"]
         (game/get-legal-commands))))

(deftest test-legal?
  (is (= true (game/legal? "save")))
  (is (= true (game/legal? "exit")))
  (is (= false (game/legal? "y"))))

