package awa.geoindex

import awa.model.data.Position
import com.google.common.geometry.S2Cell
import com.google.common.geometry.S2CellId

opaque type Cell = S2Cell

object Cell:

  def apply(position: Position, level: Int): Cell =
    S2Cell(
      S2CellId
        .fromPoint(s2Point(position))
        .parent(level),
    )

  extension (self: Cell)

    def id: Long = self.id().id()

    def position: Long = self.id().pos()

    def face: Int = self.face()

    def level: Byte = self.level()

    def index: CellIndex =
      CellIndex.of(self.id())

    def contains(cellIndex: CellIndex): Boolean =
      self.id.contains(cellIndex.value)
