package awa.geoindex

import awa.model.data.Position
import com.google.common.geometry.S2Cell
import com.google.common.geometry.S2CellId
import com.google.common.geometry.S2LatLng
import com.google.common.geometry.S2Point

private[geoindex] def s2Point(position: Position): S2Point =
  s2LatLng(position).toPoint()

private[geoindex] def s2LatLng(position: Position): S2LatLng =
  S2LatLng.fromDegrees(position.y, position.x)

opaque type GeoId = S2CellId

object GeoId:

  private[geoindex] def of(self: S2CellId): GeoId = self

  def apply(position: Position, level: Int): GeoId =
    S2CellId
      .fromLatLng(s2LatLng(position))
      .parent(level)

  extension (self: GeoId)
    private[geoindex] def value: S2CellId = self
    def contains(id: GeoId): Boolean      = self.contains(id: S2CellId)
    def id: Long                          = self.id()
    def level: Int                        = self.level()
    def parent: GeoId                     = self.parent()
    def token: String                     = self.toTokenOld()

opaque type GeoCell = S2Cell

object GeoCell:
  extension (self: GeoCell)
    private[geoindex] def value: S2Cell = self
    def id: Long                        = self.id().id()
    def position: Long                  = self.id().pos()
    def face: Int                       = self.face()
    def level: Byte                     = self.level()
    def index: GeoId                    = GeoId.of(self.id())
    def token: String                   = self.id().toToken()
