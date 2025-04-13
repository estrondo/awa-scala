package awa.testing.data

import awa.testing.Fixture
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineString
import scala.util.Random

object LineStringFixture extends Fixture:

  def createRandom(length: Int, random: Random = Random()): LineString =
    val coordinates = (0 until length).map { _ =>
      Coordinate(random.nextDouble(), random.nextDouble())
    }.toArray

    Fixture.geometryFactory
      .createLineString(Fixture.coordinateSequenceFactory.create(coordinates))
