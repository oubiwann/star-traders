(import itertools)

(import (starlanes (config player util)))


(defun get-next-company (game-data)
  (setv current-count (len game-data.companies))
  (try
    (.next
      (itertools.islice
          (get-companies)
          current-count
          (+ current-count 1)))
    (except (, StopIteration)
      None)))

(defun update-player-shares (player company-name shares)
  (if (not player.stock)
    (setv player.stock {}))
  (if (not (in company-name player.stock))
    (player.stock.update {company-name 0}))
  (+= (get player.stock company-name) shares))

(defun update-company-share-value (company-name share-value game-data)
  (if (not (in company-name game-data.share-value))
    (game-data.share-value.update {company-name 0}))
  (+= (get game-data.share-value company-name) share-value))

(defun -create-company (company-name share-value game-data)
  (setv current-player (player.get-current-player game-data))
  (game-data.companies.append company-name)
  (util.clear-screen)
  (print-company-announcement company-name)
  (update-player-shares
    current-player
    company-name
    config.founding-shares)
  (update-company-share-value
    company-name
    config.share-modifier-star
    game-data)
  (print-stock-grant-notice)
  (raw-input config.continue-prompt)
  (car company-name))

(defun create-company-star (game-data)
  (setv company-name (get-next-company game-data))
  (if company-name
    (-create-company
      company-name
      config.share-modifier-star
      game-data)
    config.outpost-char))

(defun create-company-outpost (game-data)
  (setv company-name (get-next-company game-data))
  (if company-name
    (-create-company
      company-name
      config.share-modifier-outpost
      game-data)
    config.outpost-char))

(defun print-stock-grant-notice ()
  (print "You have been granted a founder's reward of"
         config.founding-shares
         "shares!\n"))

(defun print-company-announcement (company-name)
  (print (+ config.beep "A new shipping company has been formed!"))
  (print (+ "It's name is '" company-name "'.\n")))

(defun get-company-char (company)
  (car company))

(defun get-companies (&optional (initials false))
  (setv max-companies (zip (xrange config.max-companies) config.companies))
  (for ((, index company) max-companies)
    (if (not initials)
      (yield company)
      (yield (car company)))))

(defun display-assets (game-data)
  (setv current-player (player.get-current-player game-data))
  (setv assets current-player.cash)
  (setv stock-data [])
  (if current-player.stock
    (setv stock-data current-player.stock.items))
  (util.clear-screen)
  (print (+ current-player.name ", these are your current assets:\n"))
  (print "CASH")
  (print "\tAmount\n\t------")
  (print (+ "\t$" (str current-player.cash) "\n"))
  (print "STOCK")
  (print "\tCompany\t\t\tShares\tPrice/Share\tTotal Value")
  (print "\t-------\t\t\t------\t-----------\t-----------\n")
  (for ((, company-name stock) stock-data)
    (setv share-value (get game-data.share-value company-name))
    (setv total-value (* stock share-value))
    (+= assets total-value)
    (print (+ "\t" company-name
              "\t\t" (str stock)
              "\t$" (str share-value)
              "\t\t$" (str total-value))))
  (print "\nTOTAL ASSET VALUE\n")
  (print (+ "\t$" (str assets)))
  (print "\n")
  (raw-input config.continue-prompt))
