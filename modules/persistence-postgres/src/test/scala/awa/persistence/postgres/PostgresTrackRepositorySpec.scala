package awa.persistence.postgres

import awa.model.Track
import awa.persistence.TrackRepository
import awa.test.testcontainers.Container
import awa.test.testcontainers.PostgresDataSource
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomTrack
import io.getquill.PostgresZioJdbcContext
import io.getquill.SnakeCase
import javax.sql.DataSource
import zio.Scope
import zio.ZIO
import zio.ZLayer
import zio.test.*

object PostgresTrackRepositorySpec extends Spec:

  def spec = suite(nameOf[PostgresTrackRepository.type])(
    test(s"It should insert a ${nameOf[Track]} into database.") {
      check(AwaGen.randomTrack) { track =>
        for result <- ZIO.serviceWithZIO[TrackRepository](_.add(track))
        yield assertTrue(
          result == track,
        )
      }
    },
  ).provideSome[Scope](
    Container.postgresContainer,
    PostgresDataSource.fromPostgresContainer,
    PostgresDataSource.dataSourceAsZLayer,
    PostgresDataSource.zioContext,
    ZLayer.fromFunction(PostgresTrackRepository.apply),
  ) @@ TestAspect.sequential
