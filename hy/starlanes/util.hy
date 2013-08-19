(import os)
(import re)

(import (starlanes (config)))


(defun clear-screen ()
  (os.system "clear"))

(defun move->coords (move)
  (setv match (re.match "([0-9]+)(.*)" move))
  [(get (match.groups) 1) (int (get (match.groups) 0))])

(defun filter-allowed (all legal)
  (list-comp x (x all) (in x legal)))

(defun -get-valid-coords (coords min max)
  (filter-allowed
    coords
    (xrange min (+ max 1))))

(defun get-valid-x-coords (coords)
  (-get-valid-coords
    coords
    config.xgrid-start
    config.xgrid-end))

(defun get-valid-y-coords (coords)
  (-get-valid-coords
    coords
    config.ygrid-start
    config.ygrid-max))

(defclass Counter (object)
  ((count 0)

   (--init-- (fn (self &optional (base 10))
    (setv self.parent None)
    (setv self.base base)
    None))

   (tick (fn (self)
    (+= self.count 1)
    (if (= 0 (% self.count self.base))
      (do
        (if (= None self.parent)
          (setv self.parent (Counter)))
        (setv self.count 0)
        (self.parent.tick)))))))
