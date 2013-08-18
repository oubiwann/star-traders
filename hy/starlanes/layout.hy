(import random)
(import itertools)

(import (starlanes (config game util)))


(defun print-grid-title (buffer-width grid-width)
  (let ((mid-point (/ grid-width 2))
        (title-len (len config.game-title))
        (start-point (+ 1 (- mid-point (/ title-len 2))))
        (fill (* config.space (int start-point)))
        (buffer (* config.space buffer-width))
        (separator (* config.horiz-title-heading-char title-len)))
    (print (+ buffer fill config.game-title))
    (print (+ buffer fill separator config.new-line))))

(defun get-header ()
  (.join config.row-heading-init config.xgrid))

(defun get-row-header (row-grid-entry)
  (+ row-grid-entry config.row-heading-init))

(defun get-row-header-buffer ()
  (len (get-row-header (max config.ygrid))))

(defun print-xgrid-headers (buffer-length)
  (let ((header (get-header))
        (separator (*  config.horiz-divider-char (len header))))
    (print (+ (* config.space buffer-length) header))
    (print (+ (* config.space (- buffer-length 2))
              config.horiz-divider-init
              separator
              config.horiz-divider-term))))

(defun fill-new-map (x y)
  (cond ((<= (random.random) config.star-rate)
          (do
            (.update config.star-map {(, y x) config.star-char})
            config.star-char))
        (true
          (do
            (.update config.star-map {(, y x) config.grid-point})
            config.grid-point))))

(defun get-row (fill-function row-num)
  (let ((row-data (list)))
    (for (element (get-header))
      (cond ((in element config.xgrid)
              (.append row-data (fill-function element row-num)))
            (true
              (.append row-data config.space))))
    (+ (.join config.null-string row-data)
       config.row-heading-term)))

(defun print-rows (fill-function)
  (for (row-header config.ygrid)
    (print (+ (get-row-header row-header)
              (get-row fill-function row-header)))))

(defun grouper (a)
  (car
    (car a)))

(defun print-map-rows (None)
  (for ((, row-header items) (itertools.groupby (game.get-map-items) grouper))
    (setv row-data (list))
    (for ((, coords item) items)
        (row-data.append item))
    (print (+ (get-row-header row-header)
              (.join (* 3 config.space) row-data)
              config.row-heading-term))))

(defun print-footer (buffer-length footer-length)
  (print (+ (* config.space (- buffer-length 2))
            config.horiz-divider-init
            (* config.horiz-divider-char footer-length)
            config.horiz-divider-term)))

(defun draw-grid (row-function fill-function)
  (util.clear-screen)
  (let ((buffer-length (get-row-header-buffer))
        (header-length (len (get-header))))
    (print-grid-title buffer-length header-length)
    (print-xgrid-headers buffer-length)
    (row-function fill-function)
    (print-footer buffer-length header-length)))

(defun draw-new-grid ()
  (draw-grid print-rows fill-new-map))

(defun redraw-grid ()
  (draw-grid print-map-rows None))