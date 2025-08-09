package awa.geoindex

import awa.model.TrackSegment
import awa.model.data.SegmentPath
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.trackSegment
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import zio.Scope
import zio.ZIO
import zio.test.TestEnvironment
import zio.test.assertTrue

object GeoIndexerSpec extends Spec:

  def extract(indexed: IndexedGeoId) = (indexed.index, indexed.id.token)

  override def spec = suite(nameOf[GeoIndexer])(
    test(s"It should index a specifc ${nameOf[TrackSegment]}.") {
      for
        original        <- AwaGen.trackSegment.oneSample
        geometryFactory <- ZIO.service[GeometryFactory]
        updated          = original.copy(
                             path = SegmentPath(
                               geometryFactory.createLineString(
                                 Array(
                                   Coordinate(0, 0),     // 0
                                   Coordinate(90, 0),    // 1
                                   Coordinate(0, 90),    // 2
                                   Coordinate(-180, 90), // 2
                                   Coordinate(180, 0),   // 3
                                   Coordinate(-180, 0),  // 3
                                   Coordinate(-90, 0),   // 4
                                   Coordinate(0, -90),   // 5
                                   Coordinate(180, -90), // 5
                                 ),
                               ),
                             ),
                           )
      yield assertTrue(
        GeoIndexer(updated).map(extract) == Seq(
          (0, "10000000004"),
          (1, "30000000004"),
          (2, "50000000004"),
          (4, "6fffffffffc"),
          (5, "70000000004"),
          (6, "90000000004"),
          (7, "b0000000004"),
        ),
      )
    },
  ).provideLayer(geometryFactoryLayer)
