(ns starlanes.finance
  (:require [starlanes.const :as const]
            [starlanes.game.map :as game-map]
            [starlanes.util :as util]))


(defn company-factory []
  {:name ""
   :share-mod 0.0
   :units 0})

(defn get-new-company
  ([]
    (company-factory))
  ([name units share-mod]
    (assoc (company-factory) :name name :units units :share-mod share-mod)))

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

(defn get-next-company [game-data]
  (let [current-count (count (game-data :companies))
        next-index (inc current-count)])
  )

(defn add-company [units share-mod game-data]
  (let [available (game-data :companies-queue)
        company-name (first available)
        game-data (conj game-data {:companies-queue (rest available)})
        company (get-new-company company-name units share-mod)
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

(defn announce-new-company [company-name]
  (util/beep)
  (util/display
    (str
      "A new shipping company has been formed!" \newline
      "It's name is '" company-name "'." \newline)))

(defn announce-player-bonus [current-player company-name units mod]
  (util/display
    (str
      \newline
      (current-player :name) ", you have been awarded "
      const/founding-shares " share(s) in " \newline
      company-name ", currently valued at " \newline
      (* units mod) " "
      const/currency-name "s each." \newline \newline))
  (util/input const/continue-prompt))

(defn make-announcements [current-player company-name units mod]
  (announce-new-company company-name)
  (announce-player-bonus current-player company-name units mod))

(defn update-player-stock [current-player units game-data]
  game-data
  )

(defn create-company
  "Creating a company should only ever happen if all attempts have been made
  to identify possibly merges, first. As such, there should never be another
  company in the immediate neighborhood (adjoining spaces) of a new company;
  there should only be stars and/or outposts."
  [keyword-coord current-player share-modifier game-data]
  (let [outpost-coords (map first (game-map/get-neighbor-outposts
                                    keyword-coord game-data))
        units (count outpost-coords)
        [company-name game-data] (add-company units share-modifier game-data)
        item-char (str (first company-name))]
    (make-announcements
      current-player company-name (inc units) share-modifier)
    (update-player-stock
      current-player
      units
      (game-map/multi-update-coords
        (concat outpost-coords [keyword-coord])
        item-char
        game-data))))

(defn create-star-company
  "Update the game data with a new company created from adjacent outposts."
  [keyword-coord current-player game-data]
  (create-company
    keyword-coord current-player const/share-modifier-star game-data))

(defn create-outpost-company
  "Update the game data with a new company created from adjacent outposts."
  [keyword-coord current-player game-data]
  (create-company
    keyword-coord current-player const/share-modifier-outpost game-data))

(defn update-company-share-mod []
  )

(defn merge-companies
  ""
  [keyword-coord current-player companies game-data]
  (util/display (str \newline "Merging companies ..." \newline))
  (util/input const/continue-prompt)
  game-data)

(defn expand-company
  ""
  [keyword-coord current-player company-item-data game-data]
  (let [company-letter (second company-item-data)
        outpost-coords (map first (game-map/get-neighbor-outposts
                                    keyword-coord game-data))]
    ; XXX this is an insufficient final solution; see the following issue for
    ; more details:
    ;   https://github.com/oubiwann/star-traders/issues/7
    ; we're going to want to create a function that takes a list of neighbor
    ; outposts, converts them to the company, and then recursing on all those
    ; outposts' neighbors that are outposts, performing the same action
    (game-map/multi-update-coords
      (concat outpost-coords [keyword-coord])
      company-letter
      game-data)))



