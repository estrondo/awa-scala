package awa.model.data

opaque type HorizontalAccuracy = Int

object HorizontalAccuracy:
  def apply(value: Int): HorizontalAccuracy            = value
  extension (value: HorizontalAccuracy) def value: Int = value
