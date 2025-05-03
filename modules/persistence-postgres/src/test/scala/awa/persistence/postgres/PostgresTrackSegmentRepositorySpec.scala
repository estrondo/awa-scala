package awa.persistence.postgres

import awa.generator.TimeGenerator
import awa.model.Track
import awa.persistence.TrackSegmentRepository
import awa.scalamock.ZIOStubBaseOperations
import awa.test.testcontainers.Container
import awa.test.testcontainers.PostgresDataSource
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomTrackSegment
import io.getquill.PostgresZioJdbcContext
import io.getquill.SnakeCase
import javax.sql.DataSource
import org.locationtech.jts.geom.GeometryFactory
import org.scalamock.stubs.ZIOStubs
import zio.Scope
import zio.ZIO
import zio.ZLayer
import zio.test.*
import awa.testing.generator.randomOrder

object PostgresTrackSegmentRepositorySpec extends Spec, ZIOStubBaseOperations, ZIOStubs:

  def spec = suite(nameOf[PostgresTrackSegmentRepository.type])(
    test(s"It should insert a ${nameOf[Track]} into database.") {
      check(
        for
          input <- AwaGen.randomTrackSegment
          now   <- AwaGen.nowZonedDateTime
          order <- AwaGen.randomOrder
        yield (
          input.copy(order = Some(order)),
          now,
        ),
      ) { (trackSegment, now) =>
        for
          _      <- stubLayerWith[TimeGenerator] { generator => 
                      (() => generator.now()).returns(now)
                    }
          result <- ZIO.serviceWithZIO[TrackSegmentRepository](_.add(trackSegment))
        yield assertTrue(
          result == trackSegment,
        )
      }
    },
  ).provideSome[Scope](
    stubLayer[TimeGenerator],
    geometryFactoryLayer,
    Container.postgresContainer,
    PostgresDataSource.fromPostgresContainer,
    PostgresDataSource.dataSourceAsZLayer,
    PostgresDataSource.zioContext,
    ZLayer.fromFunction(PostgresTrackSegmentRepository.apply),
  ) @@ TestAspect.sequential
