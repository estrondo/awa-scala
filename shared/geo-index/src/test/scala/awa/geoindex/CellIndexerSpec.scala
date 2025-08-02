package awa.geoindex

import awa.model.data.SegmentPath
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.trackSegment
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import zio.Scope
import zio.ZIO
import zio.test.TestAspect
import zio.test.TestEnvironment
import zio.test.assertTrue
import zio.test.check

object CellIndexerSpec extends Spec:

  def extract(cell: Cell) = (cell.face, cell.level, cell.position)

  override def spec = suite(nameOf[CellIndexer])(
    test("It should index a specifc TrackSegment") {

      check(AwaGen.trackSegment) { original =>
        for
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
          CellIndexer(updated).map(extract(_)) == Seq(
            (0, 13, 1152921521786716160L),
            (1, 13, 1152921521786716160L),
            (2, 13, 1152921521786716160L),
            (3, 13, 1152921487426977792L),
            (3, 13, 1152921521786716160L),
            (4, 13, 1152921521786716160L),
            (5, 13, 1152921521786716160L),
            (5, 13, 1152921521786716160L),
          ),
        )
      }
    } @@ TestAspect.samples(1),
  ).provideLayer(geometryFactoryLayer)
