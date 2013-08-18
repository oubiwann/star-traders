(import itertools)

(import (starlanes (config)))


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

(defun create-company (game-data)
  )

(defun print-company-announcement (game-data)
  (print "A new shipping company has been formed!")
  (print (+ "It's name is '"
            (get-next-company game-data)
            "'")))

(defun get-company-char (company)
  (car company))

(defun get-companies (&optional (initials false))
  (setv max-companies (zip (xrange config.max-companies) config.companies))
  (for ((, index company) max-companies)
    (if (not initials)
      (yield company)
      (yield (car company)))))

(defun display-stock (player)
  )