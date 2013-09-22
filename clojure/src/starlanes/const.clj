(ns starlanes.const)


(def game-title "Star Traders")

(def items {:star "*" :empty "." :outpost "+"})

(def outpost-char "+")
(def grid-spaces 3)

(def xgrid-start (int (.charAt "a" 0)))
(def xgrid-end (inc (int (.charAt "e" 0))))
(def xgrid-size (- xgrid-end xgrid-start))
(def xgrid
  (map
    (comp str char)
    (range xgrid-start xgrid-end)))

(def ygrid-start 1)
(def ygrid-end 5)
(def ygrid-pad (count (str ygrid-end)))

(defn make-y-coord [y]
  (format (str "%0" ygrid-pad "d") (Integer. y)))

(def ygrid-end (+ ygrid-start ygrid-end))

(def ygrid
  (map str (range ygrid-start ygrid-end)))

(def horiz-title-heading-char "=")
(def horiz-divider-char "-")
(def horiz-divider-init (str "+" horiz-divider-char))
(def horiz-divider-term (str horiz-divider-char "+"))

(def vert-divider-char "|")

(def row-heading-init (str \space vert-divider-char \space))
(def row-heading-term (str \space vert-divider-char))

(def win-by-turns? false)
(def max-turns 2)
(def seed 314)
; mex-moves-choices is the maximum allowed number of randomly-generated moves
; that will be offered to the player as options during a turn. By default,
; users are presented with 5 choices from which they can select their move.
(def max-moves-choices 5)
(def star-rate 0.05)

(def companies ["Altair Starways"
                "Betelgeuse, Ltd."
                "Capella Cargo Co."
                "Denebola Shippers"
                "Eridani Expediters"
                "Fomalhaut Freighters"
                "Gamma Corvi Cartel"
                "Horologii Holdings"
                "Innes Industries"
                "Jabbah Journeyers"
                "Kapteyn's Outfitters"
                "Luyten, Ltd."])

(def max-companies 5)
(def share-modifier-star 500)
(def share-modifier-outpost 100)
(def founding-shares 5)
(def dividend-percentage 5)
(def max-stock-value 3000)
(def currency-name "credit")

(def continue-prompt "Press <RETURN> to continue ...")
(def confirm-prompt "Are you sure? [N/y] ")

(def commands [
  {:command "stock" :alias "s" :help "show the current player's assets"}
  {:command "map" :alias "m" :help "show the star map"}
  {:command "order" :alias "o" :help "show the order of play"}
  {:command "score" :alias "" :help "show the current score"}
  {:command "save" :alias "" :help "save the current state of the game"}
  {:command "load" :alias "" :help "replace current game with saved one"}
  {:command "commands" :alias "c" :help "display list of available commands"}
  {:command "help" :alias "h" :help "display commands information"}
  {:command "restart" :alias "" :help "restart the game"}
  {:command "quit" :alias "q" :help "shutdown the game; same as 'exit'"}
  {:command "exit" :alias "x" :help "shutdown the game; same as 'quit'"}])

