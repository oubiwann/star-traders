(ns starlanes.version)

(def STARLANES-VERSION {:major 0
                        :minor 1
                        :patch 0
                        :snapshot true})

(def STARLANES-VERSION-STR
  (let [version STARLANES-VERSION]
    (str "v"
         (:major version)
         "."
         (:minor version)
         (if-not (zero? (:patch version)) (str "." (:patch version)) "")
         (if (:snapshot version) "-dev" ""))))