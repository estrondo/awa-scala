package awa.persistence.postgres

import awa.generator.TimeGenerator
import awa.model.Track
import awa.persistence.TrackSegmentRepository
import awa.scalamock.ZIOStubBaseOperations
import awa.test.testcontainers.Container
import awa.test.testcontainers.PostgresDataSource
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.order
import awa.testing.generator.trackSegment
import io.getquill.PostgresZioJdbcContext
import io.getquill.SnakeCase
import javax.sql.DataSource
import org.locationtech.jts.geom.GeometryFactory
import org.scalamock.stubs.ZIOStubs
import zio.Scope
import zio.ZIO
import zio.ZLayer
import zio.test.*
import awa.sbt.AwaPostgreSQLTest

object PostgresTrackSegmentRepositorySpec extends Spec, ZIOStubBaseOperations, ZIOStubs:

  def spec = suite(nameOf[PostgresTrackSegmentRepository.type])(
    test(s"It should insert a ${nameOf[Track]} into database.") {
      check(
        for
          input <- AwaGen.trackSegment
          now   <- AwaGen.nowZonedDateTime
          order <- AwaGen.order
        yield (
          input.copy(order = Some(order)),
          now,
        ),
      ) { (trackSegment, now) =>
        for
          _      <- stubLayerWith[TimeGenerator] { generator =>
                      (() => generator.now()).returnsWith(now)
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
    Container.postgresContainer(AwaPostgreSQLTest.image),
    PostgresDataSource.fromPostgresContainer,
    PostgresDataSource.dataSourceAsZLayer,
    PostgresDataSource.zioContext,
    ZLayer.fromFunction(PostgresTrackSegmentRepository.apply),
  ) @@ TestAspect.sequential
