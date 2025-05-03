package awa.crs

import awa.model.data.Segment
import org.geotools.referencing.GeodeticCalculator

object Geodesic:

  def length(segment: Segment): Int =
    // I wanted to use a classic old while here!
    val calculator  = GeodeticCalculator(geoCentricCoordinateReferenceSystem)
    val coordinates = segment.value.getCoordinateSequence
    val maxIndex    = coordinates.size()
    var length      = 0d

    if maxIndex > 0 then
      var previous = coordinates.getCoordinate(0)
      var index    = 1
      while index < maxIndex do
        val current = coordinates.getCoordinate(index)
        calculator.setStartingGeographicPoint(previous.x, previous.y)
        calculator.setDestinationGeographicPoint(current.x, current.y)
        length += calculator.getOrthodromicDistance
        previous = current
        index += 1

    length.toInt
