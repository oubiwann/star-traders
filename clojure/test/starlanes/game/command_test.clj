(ns starlanes.game.command-test
  (:require [clojure.test :refer :all]
            [starlanes.game.command :as game]
            [starlanes.util :as util]))


(deftest test-get-commands
  (is (= '(("commands" "c") ("exit" "x") ("help" "h") ("load") ("map" "m")
           ("order" "o") ("restart") ("quit" "q") ("save") ("score")
           ("stock" "s"))
         (game/get-commands)))
  (is (= ["stock" "s"] (game/get-commands "stock")))
  (is (= ["load"] (game/get-commands "load"))))

(deftest test-get-legal-commands
  (is (= ["commands" "c" "exit" "x" "help" "h" "load" "map" "m" "order" "o"
          "restart" "quit" "q" "save" "score" "stock" "s"]
         (game/get-legal-commands))))

(deftest test-legal?
  (is (= true (game/legal? "save")))
  (is (= true (game/legal? "exit")))
  (is (= false (game/legal? "y"))))

