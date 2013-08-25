(ns starlanes.util)


(defn display [data]
  (.print (System/out) data))

(defn clear-screen []
  (display "\u001b[2J")
  (display "\u001B[0;0f"))

(defn input [prompt]
  (display prompt)
  (read-line))

(defn mult-str [amount string]
  (apply str (repeat amount string)))

(defn ord [chr]
  (int (.charAt chr 0)))

(defn random [seed]
  (proxy [java.util.Random][seed]
    (next [a] (proxy-super next a))))

(defn rand-float [random]
  (.nextFloat random))

(defn rand-game [game-data]
  (rand-float (:rand game-data)))