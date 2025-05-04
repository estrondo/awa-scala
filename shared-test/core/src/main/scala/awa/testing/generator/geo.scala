package awa.testing.generator

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.PrecisionModel
import zio.test.Gen

private val factory = GeometryFactory(PrecisionModel(PrecisionModel.FLOATING), 4326)

private val rad = math.Pi * 2

extension (gen: AwaGen)

  def geometryFactory: GeometryFactory = factory

  def lineString(rMin: Double, rMax: Double, pMin: Int, pMax: Int): Gen[Any, LineString] =
    val r = math.abs(rMax)
    gen.lineString(
      -180 + r,
      180 - r,
      -90 + r,
      90 - r,
      rMin,
      rMax,
      pMin,
      pMax,
    )

  def lineString(
      xMin: Double,
      xMax: Double,
      yMin: Double,
      yMax: Double,
      rMin: Double,
      rMax: Double,
      pMin: Int,
      pMax: Int,
  ): Gen[Any, LineString] =

    require(pMin > 1)
    require(pMax >= pMin)

    def genCoordinate(angle: Double, step: Double, x: Double, y: Double): Gen[Any, (Double, Coordinate)] =
      for r <- Gen.double(rMin, rMax)
      yield
        val sin = math.sin(angle)
        val cos = math.cos(angle)
        (angle + step, Coordinate(x + r * sin, y + r * cos, 0d))

    for
      p           <- Gen.oneOf(Gen.const(0), Gen.int(pMin, pMax))
      x           <- Gen.double(xMin, xMax)
      y           <- Gen.double(yMin, yMax)
      starting    <- Gen.double(0d, rad)
      ending       = starting + rad
      step         = rad / p
      coordinates <- p match
                       case 0 => Gen.const(List.empty[Coordinate])
                       case _ =>
                         Gen.unfoldGenN(p)(starting) { angle =>
                           if angle <= ending then genCoordinate(angle, step, x, y)
                           else Gen.empty
                         }
    yield
      if coordinates.size == p then geometryFactory.createLineString(coordinates.toArray)
      else throw IllegalStateException("coordinate.size != p")
