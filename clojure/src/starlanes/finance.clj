(ns starlanes.finance
  (:require [starlanes.const :as const]
            [starlanes.player :as player]
            [starlanes.util :as util]))


(defn display-stock [game-data]
  (util/display
    (str \newline "Here's your current stock: " \newline \newline))
  (util/input const/continue-prompt)
  nil)

(defn compute-stock-value [{stock :stock value :value}]
  (* stock value))

(defn compute-stocks-value [stocks-data]
  (reduce + (map compute-stock-value stocks-data)))

(defn compute-value
  "The assets parameter is a mapwhich has the following structure:
    {:cash <float> :stock <integer> :value <float>}
  where :value is stock price of the associated stock.
  "
  ([assets]
    (apply compute-value assets))
  ([cash stocks-data]
    (+ cash (compute-stocks-value stocks-data))))

(defn display-value [value]
  )

