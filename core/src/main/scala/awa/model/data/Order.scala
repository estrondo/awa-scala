package awa.model.data

opaque type Order = Int

object Order:
  def apply(value: Int): Order            = value
  extension (value: Order) def value: Int = value
