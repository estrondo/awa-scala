package awa.geoindex

import awa.model.data.Position
import com.google.common.geometry.S2LatLng
import com.google.common.geometry.S2Point

private[geoindex] def s2Point(position: Position): S2Point = 
  val point = S2LatLng.fromDegrees(position.y, position.x).toPoint()
  point

private[geoindex] def s2LatLng(position: Position): S2LatLng = S2LatLng.fromDegrees(position.y, position.x)
