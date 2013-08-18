(setv game-title "Star Traders")

(setv null-string "")
(setv space " ")
(setv new-line "\n")
(setv grid-point ".")
(setv star-char "*")
(setv outpost-char "+")

(setv xgrid-start (ord "a"))
(setv xgrid-end (+ (ord "d") 1))
(setv xgrid-size (- xgrid-end xgrid-start))
(setv xgrid
  (list-comp
    (chr x)
    (x (xrange xgrid-start xgrid-end))))

(setv ygrid-start 1)
(setv ygrid-max 4)
(setv ygrid-pad (len (str ygrid-max)))

(defun make-y-coord (int)
  (.zfill (str int) ygrid-pad))

(setv ygrid-end (+ ygrid-start ygrid-max))
(setv ygrid
  (list-comp
    (make-y-coord y)
    (y (xrange ygrid-start ygrid-end))))

(setv horiz-title-heading-char "=")
(setv horiz-divider-char "-")
(setv horiz-divider-init (+ "+" horiz-divider-char))
(setv horiz-divider-term (+ horiz-divider-char "+"))

(setv vert-divider-char "|")

(setv row-heading-init (+ space vert-divider-char space))
(setv row-heading-term (+ space vert-divider-char))

(setv win-by-turns false)
(setv max-turns 2)
(setv seed 123)
(setv max-moves 5)
(setv star-rate 0.05)
(setv star-map (dict))

(setv company-1 "Altair Starways")
(setv company-2 "Betelgeuse, Ltd.")
(setv company-3 "Capella Cargo Co.")
(setv company-4 "Denebola Shippers")
(setv company-5 "Eridani Expediters")
(setv company-6 "Fomalhaut Freighters")
(setv company-7 "Gamma Corvi Cartel")
(setv company-8 "Horologii, Inc.")
(setv company-9 "Innes Industries")
(setv company-10 "Jabbah Journeyers")
(setv companies [company-1 company-2 company-3 company-4 company-5
                 company-6 company-7 company-8 company-9 company-10])
(setv max-companies 5)
(setv share-modifier-star 500)
(setv share-modifier-outpost 100)
(setv founding-shares 5)
(setv divident-percentage 5)
(setv max-stock-value 3000)

