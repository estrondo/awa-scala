package awa.test.testcontainers

import java.time.Duration
import java.time.temporal.ChronoUnit
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.utility.DockerImageName
import zio.Scope
import zio.Tag
import zio.ZIO
import zio.ZLayer

object Container:

  // noinspection ScalaWeakerAccess
  def layerOf[W <: ContainerWrapper: Tag](
      wrapper: => W,
  ): ZLayer[Scope, Throwable, W] =
    val acquire = ZIO.attemptBlocking {
      val w = wrapper
      w.container.start()
      w
    }

    val release = (wrapper: W) => {
      ZIO.attemptBlocking(wrapper.container.stop()).ignoreLogged
    }

    ZLayer {
      ZIO.acquireRelease(acquire)(release)
    }

  val postgresContainer: ZLayer[Scope, Throwable, PostgreSQLContainerWrapper] = layerOf {
    val container = containers.PostgreSQLContainer(
      DockerImageName
        .parse("docker.io/library/estrondo:awa-test-postgres-0.0.0")
        .asCompatibleSubstituteFor("postgres"),
    )

    container.withUsername("awa")
    container.withPassword("awa")
    container.withDatabaseName("awa-test")
    container.setWaitStrategy(
      LogMessageWaitStrategy()
        .withRegEx(""".*database system is ready to accept connections.*\s""")
        .withTimes(1)
        .withStartupTimeout(Duration.of(10, ChronoUnit.SECONDS)),
    )

    PostgreSQLContainerWrapper(container)
  }
