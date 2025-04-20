package awa.test.testcontainers

import awa.AwaException
import io.getquill.PostgresZioJdbcContext
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import javax.sql.DataSource
import org.postgresql.ds.PGSimpleDataSource
import zio.ULayer
import zio.ZIO
import zio.ZLayer

object PostgresDataSource:

  val fromPostgresContainer: ZLayer[PostgreSQLContainerWrapper, Throwable, DataSource] =
    ZLayer.environment[PostgreSQLContainerWrapper].flatMap { env =>
      Quill.DataSource.fromDataSource {
        val wrapper    = env.get[PostgreSQLContainerWrapper]
        val dataSource = PGSimpleDataSource()
        dataSource.setUrl(wrapper.container.getJdbcUrl)
        dataSource.setUser(wrapper.container.getUsername)
        dataSource.setPassword(wrapper.container.getPassword)
        dataSource
      }
    }

  val dataSourceAsZIO: ZLayer[DataSource, Throwable, ZIO[Any, AwaException, DataSource]] =
    ZLayer {
      ZIO.serviceWith[DataSource](ZIO.succeed(_))
    }

  val zioContext: ULayer[PostgresZioJdbcContext[SnakeCase]] = ZLayer.succeed(PostgresZioJdbcContext(SnakeCase))
