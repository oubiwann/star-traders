(ns starlanes.finance
  (:require [starlanes.const :as const]
            [starlanes.game.map :as game-map]
            [starlanes.util :as util]))


(defn company-factory []
  {:name ""
   :share-mod 0.0
   :units 0})

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
  where :value is stock price of the associated stock."
  ([assets]
    (apply compute-value assets))
  ([cash stocks-data]
    (+ cash (compute-stocks-value stocks-data))))

(defn display-value [value]
  )

(defn get-companies []
  (take const/max-companies const/companies))

(defn get-next-company [game-data]
  (let [current-count (count (game-data :companies))
        next-index (inc current-count)])
  )

(defn create-new-company [name units share-mod]
  (assoc (company-factory) :name name :units units :share-mod share-mod))

(defn add-company [units share-mod game-data]
  (let [available (game-data :companies-queue)
        company-name (first available)
        game-data (conj game-data {:companies-queue (rest available)})
        company (create-new-company company-name units share-mod)
        companies (concat (game-data :companies) [company])]
    [company-name (conj game-data {:companies companies})]))

(defn filter-company
  "Reomve company data whose names match the passed name."
  [company-name companies]
  (filter
    (fn [x]
      (not= (x :name) company-name))
    companies))

(defn remove-company [company-name game-data]
  (let [companies-queue (sort
                          (concat
                            (game-data :companies-queue) [company-name]))
        companies (filter-company company-name (game-data :companies))]
    (conj game-data {:companies companies :companies-queue companies-queue})))

(defn announce-new-company [company-name units type]
  (util/beep)
  (util/display
    (str
      "A new company has been formed!" \newline \newline
      company-name " was founded by " units
      " " type "." \newline
      )))

(defn announce-player-bonus [current-player company-name units mod]
  (util/display
    (str
      \newline (current-player :name)
      ", you have been awarded " units " share(s) in " \newline
      company-name ", currently valued at " \newline
      (* units mod) " "
      const/currency-name "s each." \newline \newline))
  (util/input const/continue-prompt))

(defn make-announcements [current-player company-name units mod type]
  (announce-new-company company-name units type)
  (announce-player-bonus current-player company-name units mod))

(defn update-player-stock [current-player units game-data]
  game-data
  )

(defn create-star-company
  "Update the game data with a new company created from adjacent outposts."
  [keyword-coord current-player game-data]
  (let [units 1
        [company-name game-data] (add-company
                                   units
                                   const/share-modifier-star
                                   game-data)
        item-char (str (first company-name))]
    (make-announcements
      current-player company-name units const/share-modifier-star
      "star-proximity base")
    (update-player-stock
      current-player
      units
      (game-map/update-coords
        keyword-coord
        item-char
        game-data))))

(defn create-outpost-company
  "Update the game data with a new company created from adjacent outposts."
  [keyword-coord current-player game-data]
  (let [outpost-coords (map first (game-map/get-neighbor-outposts
                                    keyword-coord game-data))
        units (count outpost-coords)
       [company-name game-data] (add-company
                                  units
                                  const/share-modifier-outpost
                                  game-data)
       item-char (str (first company-name))]
    (make-announcements
      current-player company-name (inc units) const/share-modifier-outpost
      "outposts")
    ;(announce-new-company company-name units "outposts")
    ;(announce-player-bonus
    ;  current-player company-name units const/share-modifier-outpost)
    (update-player-stock
      current-player
      units
      (game-map/multi-update-coords
        (concat outpost-coords [keyword-coord])
        item-char
        game-data))))

(defn update-company-share-mod []
  )

(defn perform-company-merger
  ""
  [keyword-coord current-player game-data]
  (const/items :outpost))

