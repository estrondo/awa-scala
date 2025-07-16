package awa.model.data

import org.locationtech.jts.geom.Point

opaque type Position = Point

object Position:

  def apply(self: Point): Position = self

  extension (self: Position)
    def point: Point = self
    def x: Double    = self.getX()
    def y: Double    = self.getY()
