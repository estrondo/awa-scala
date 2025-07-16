package awa.geoindex

import awa.model.data.Position
import com.google.common.geometry.S2Cell
import com.google.common.geometry.S2CellId
import com.google.common.geometry.S2LatLng

opaque type Cell = S2Cell

object Cell:

  def apply(position: Position, level: Int): Cell =
    S2Cell(
      S2CellId
        .fromLatLng(S2LatLng.fromDegrees(position.y, position.x))
        .parent(level),
    )

  extension (self: Cell)

    def id: Long = self.id().id()

    def position: Long = self.id().pos()

    def face: Int = self.face()

    def level: Byte = self.level()

    def index: CellIndex =
      CellIndex.of(self.id())

    def contains(position: Position): Boolean =
      self.contains(s2Point(position))
