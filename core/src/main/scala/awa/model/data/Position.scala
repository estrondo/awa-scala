package awa.model.data

import org.locationtech.jts.geom.Point

opaque type Position = Point

object Position:

  def apply(value: Point): Position = value

  extension (value: Position) def value: Point = value
