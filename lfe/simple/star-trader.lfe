(defmodule star-trader
  (export all))

(defrecord game-data
  users
  star-systems
  minimum-star-distance
  delay-probability
  max-bidding-rounds
  profit-margin
  )

(defrecord star-system-data
  info
  development-class
  development-increment
  )

(defrecord user-data
  trading-ship-info
  trading-ship-name
  max-ship-weight
  bank-account
  ship-speed
  )
