(ns starlanes.finance
  (:require [starlanes.const :as const]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(defn display-stock [game-data]
  (util/display
    (str \newline "Here's your current stock: " \newline \newline))
  (util/input const/continue-prompt))

