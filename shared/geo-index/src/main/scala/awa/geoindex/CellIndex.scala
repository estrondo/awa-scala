package awa.geoindex

import awa.model.data.Position
import com.google.common.geometry.S2CellId

opaque type CellIndex = S2CellId

object CellIndex:

  private[geoindex] def of(self: S2CellId): CellIndex = self

  def apply(position: Position, level: Int): CellIndex =
    S2CellId
      .fromLatLng(s2LatLng(position))
      .parent(level)

  extension (self: CellIndex)
    def id: Long                          = self.id()
    def level: Int                        = self.level()
    def parent: CellIndex                 = self.parent()
    private[geoindex] def value: S2CellId = self
