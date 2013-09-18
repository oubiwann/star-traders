(ns starlanes.const)


(def game-title "Star Traders")

(def items {:star "*" :empty "." :outpost "+"})

(def terminal-bell #(char 7))
(def outpost-char "+")
(def grid-spaces 3)

(def xgrid-start (int (.charAt "a" 0)))
(def xgrid-end (inc (int (.charAt "e" 0))))
(def xgrid-size (- xgrid-end xgrid-start))
(def xgrid
  (for
    [x (range xgrid-start xgrid-end)]
    (str (char x))))

(def ygrid-start 1)
(def ygrid-end 5)
(def ygrid-pad (count (str ygrid-end)))

(defn make-y-coord [y]
  (format (str "%0" ygrid-pad "d") (Integer. y)))

(def ygrid-end (+ ygrid-start ygrid-end))

(def ygrid
  (for
    [y (range ygrid-start ygrid-end)]
    (str y)))

(def horiz-title-heading-char "=")
(def horiz-divider-char "-")
(def horiz-divider-init (str "+" horiz-divider-char))
(def horiz-divider-term (str horiz-divider-char "+"))

(def vert-divider-char "|")

(def row-heading-init (str \space vert-divider-char \space))
(def row-heading-term (str \space vert-divider-char))

(def win-by-turns? true)
(def max-turns 2)
(def seed 314)
; mex-moves-choices is the maximum allowed number of randomly-generated moves
; that will be offered to the player as options during a turn. By default,
; users are presented with 5 choices from which they can select their move.
(def max-moves-choices 5)
(def star-rate 0.05)

(def company-1 "Altair Starways")
(def company-2 "Betelgeuse, Ltd.")
(def company-3 "Capella Cargo Co.")
(def company-4 "Denebola Shippers")
(def company-5 "Eridani Expediters")
(def company-6 "Fomalhaut Freighters")
(def company-7 "Gamma Corvi Cartel")
(def company-8 "Horologii, Inc.")
(def company-9 "Innes Industries")
(def company-10 "Jabbah Journeyers")
(def company-11 "Kapteyn's Outfitters")
(def company-12 "Luyten, Ltd.")
(def companies [company-1 company-2 company-3 company-4 company-5
                company-6 company-7 company-8 company-9 company-10
                company-11 company-12])
(def max-companies 5)
(def share-modifier-star 500)
(def share-modifier-outpost 100)
(def founding-shares 5)
(def dividend-percentage 5)
(def max-stock-value 3000)

(def continue-prompt "Press <RETURN> to continue ...")
(def confirm-prompt "Are you sure? [N/y] ")

(def commands ["stock" "s" "map" "m" "order" "o" "score"
               "save" "load"
               "help" "commands"
               "quit" "q" "exit" "x"])
