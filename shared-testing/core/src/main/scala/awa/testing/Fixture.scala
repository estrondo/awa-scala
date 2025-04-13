package awa.testing

import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.CoordinateSequenceFactory
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import scala.util.Random

object Fixture:

  val geometryFactory: GeometryFactory = GeometryFactory()

  def coordinateSequenceFactory: CoordinateSequenceFactory = geometryFactory.getCoordinateSequenceFactory

trait Fixture:

  extension [T](self: T) def asRandomOption(random: Random): Option[T] = Option.when(random.nextBoolean())(self)

  def createRandomString(length: Int = 10, random: Random): String =
    random.alphanumeric.take(length).mkString

  def createZonedDateTime(): ZonedDateTime =
    ZonedDateTime.now(Clock.systemUTC().getZone).truncatedTo(ChronoUnit.SECONDS)

  def createPoint(random: Random = Random): Point =
    Fixture.geometryFactory.createPoint(Coordinate(random.nextDouble(), random.nextDouble()))
