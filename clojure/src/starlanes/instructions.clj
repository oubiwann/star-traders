(ns starlanes.instructions
  (:require [clojure.string :as string]
            [starlanes.const :as const]
            [starlanes.util :as util]))

; Backstory notes:
;   2030-2080 - specialized space flight and exploratory automated colonization
;   2080-2110 - first waves of colonization
;   2110-2260 - establishment of colonies, cutting-edge technologies, emergence
;               of space-tech as vital player in solar economy (includes
;               advances in software-generated nanotech, quantum computing,
;               practical applications of M-theory, radical developments in
;               space-time modifying "propulsion")
;   2260-2310 - automated interstellar exploration
;   2310-2410 - first extrasolar colonies established; many fail
;   2420-2440 - extrasoloar colonies begin to stabilize; initial interstellar
;               trade efforts are made

; XXX expand this prelude and begin tweaking the game, once the original port
; has been completed.
(def prelude (str "
   The year is 2454, by Earth-reckoning. Interstellar
trade is in its infancy, despite interstellar travel
having been around for almost 200 years. Using the
great wealth that you and your peers have amassed, you
have each bet on the future of trade between star
systems. You each have a different vision of what that
future looks like and hope to accomplish it by
exerting influence via the establishment of interstellar
trade lanes."))

(def page-1 (str "
   Star Lanes is a game of interstellar trading.
The object of the game is to amass the greatest amount
of wealth.  This is accomplished by establishing vast,
interstellar shipping lanes, and purchasing stock in
the companies that control those trade routes.  During
the course of the game, stock appreciates in value as
the shipping companies become larger.  Also, smaller
companies can be merged into larger ones, and stock
in the smaller firm is converted into stock in the
larger one as described below.

    Each turn, the computer will present the player
with "
(str const/max-moves-choices)
" prospective spaces to occupy on a "
(str const/ygrid-end) "x" (str const/xgrid-size)
" matrix
(rows "
(str const/ygrid-start) "-" (str const/ygrid-end)
", columns "
(char const/xgrid-start) "-" (char (dec const/xgrid-end))
").  The player, after examining
the map of the galaxy to decide which space they wish
to occupy, responds with the row and column of that
space, i.e., 7e, 8a, etc.
"))

(def page-2 (str "
   1. The player can establish an unattached outpost --
if a coordinate is selected that is not adjacent to a
star, another unattached outpost, or an existing shipping
lane, this space will be designated with a '+'.  The
player will then proceed with stock transactions, as
listed below.

   2. The player can add to an existing lane -- if they
select a space that is adjacent to one -- and only one
existing shipping lane, the space they selects will be
added to that shipping lane and will be disignated with
the first letter of the company that owns that lane.
If there are any stars or unattached outposts also
adjacent to the selected space, they too will be
incorporated into the existing lane.  Each new square
adjacent to a star adds $"
(str const/share-modifier-star)
" per share, and each new
outpost adds $"
(str const/share-modifier-base)
" per share to the market value of the
stock of that company.
"))

(def page-3 (str "
   3. The player may establish a new shipping lane --
if there are less than "
(str const/max-companies)
" existing shipping lanes
established, the player may, given the proper space to
play, establish a new shipping lane.  They may do this
by occupying a space adjacent to a star or another
unattached outpost, but not adjacent to an existing
shipping lane.  If the player establishes a new shipping
lane, they are automatically issued "
(str const/founding-shares)
" shares in the new
company as a reward.  They may then proceed to buy stock
in any active company, including the one just formed, as
described below. The market value of the new stock is
established by the number of stars and occupied spaces
as described in #2 above.
"))

(def page-4 "
   4.  The player may merge two existing companies -- if
the player selects a space adjacent to two or more
existing shipping lanes, a merger occurs.  The larger
company takes over the smaller company (if both
companies are the same size prior to the merger, then
the survivor is randomly determined).  The stock of the
surviving company is increased in value according to
the number of spaces and stars added to its lane.  Each
player's stock in the defunct company is exchanged for
shares kn the survivor on a ratio of 2 for 1.  Also,
each player is paid a cash bonus proportional to the
percentage of outstanding stock they held in the defunct
company.

NOTE:  After a company becomes defunct through the merger
process, it can reappear elsewhere on the board when --
and if -- a new company is established.
")

(def page-5 (str "
   Next the computer adds stock dividends to the player's
cash on hand (5% of the market value of the stock in
their possession), and offers them the opportunity to
purchase stock kn any of the active companies on the board.
Stock may not be sold, but the market value of each
player's stock is taken into account at the end of the game
to determine the winner. If the market value of a given
stock exceeds $"
(str const/max-stock-value)
" at any time during the game, that stock
splits 2 for 1.  The price is cut in half, and the number
of shares owned by that player is doubled.

NOTE:  A player may look at their portfolio at any time
during the course of the game (during that player's turn)
by typing 'stock'.  Likewise, they can review the map of
the galaxy by typing 'map' to an input statement.
"))

(def pages [page-1 page-2 page-3 page-4 page-5])

(defn print-pages []
  (doseq [page pages]
    (util/clear-screen)
    (util/display page)
    (util/input (str \newline const/continue-prompt)))
  (util/clear-screen))

(defn display? []
  (let [print-instructions? (util/input "Does any player need instructions? ")]
    (if (.startsWith (string/lower-case print-instructions?) "y")
      (print-pages))))
