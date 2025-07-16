import sbt.*
import sbt.Keys.*
import sbtprotoc.ProtocPlugin.autoImport.PB

object Awa {

  object Repositories {
    val osgeo = resolvers += "osgeo" at "https://repo.osgeo.org/repository/release"
  }

  object Version {
    val awa            = "1.0.0"
    val zio            = "2.1.19"
    val postgresDriver = "42.7.7"
    val kafka          = "3.0.0"
    val avro4s         = "5.0.14"
    val protoQuill     = "4.8.6"
    val locationTech   = "1.20.0"
    val ducktape       = "0.2.9"
    val scalaMock      = "7.4.0"
    val testcontainers = "1.21.3"
    val zioLogging     = "2.5.0"
    val logbackClassic = "1.5.18"
    val geoTools       = "33.1"
    val googleS2       = "2.0.0"
  }

  object Dependencies {

    private def declare(modules: ModuleID*) =
      libraryDependencies ++= modules

    val zio = declare(
      "dev.zio" %% "zio"          % Version.zio,
      "dev.zio" %% "zio-streams"  % Version.zio,
      "dev.zio" %% "zio-test"     % Version.zio % Test,
      "dev.zio" %% "zio-test-sbt" % Version.zio % Test,
    )

    val kafka = {
      declare(
        "dev.zio" %% "zio-kafka" % Version.kafka,
      )
    }

    val avro4s = declare(
      "com.sksamuel.avro4s" %% "avro4s-core" % Version.avro4s,
    )

    val zioTestWithCompile = declare(
      "dev.zio" %% "zio-test"     % Version.zio,
      "dev.zio" %% "zio-test-sbt" % Version.zio,
    )

    val postgresDriver = declare(
      "org.postgresql" % "postgresql" % Version.postgresDriver,
    )

    val locationTech = declare(
      "org.locationtech.jts" % "jts-core" % Version.locationTech,
    )

    val protoQuill = declare(
      "io.getquill" %% "quill-jdbc"     % Version.protoQuill,
      "io.getquill" %% "quill-jdbc-zio" % Version.protoQuill,
    )

    val zioGrpc = declare(
      "io.grpc"               % "grpc-netty"           % "1.73.0",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    )

    val ducktape = declare(
      "io.github.arainko" %% "ducktape" % Version.ducktape,
    )

    val scalaMock = declare(
      "org.scalamock" %% "scalamock"     % Version.scalaMock % Test,
      "org.scalamock" %% "scalamock-zio" % Version.scalaMock % Test,
    )

    val scalamockWithCompile = declare(
      "org.scalamock" %% "scalamock"     % Version.scalaMock,
      "org.scalamock" %% "scalamock-zio" % Version.scalaMock,
    )

    val testContainers = declare(
      "org.testcontainers" % "postgresql" % Version.testcontainers,
      "org.testcontainers" % "kafka"      % Version.testcontainers,
    )

    val logging = declare(
      "dev.zio"       %% "zio-logging"        % Version.zioLogging,
      "dev.zio"       %% "zio-logging-slf4j2" % Version.zioLogging,
      "ch.qos.logback" % "logback-classic"    % Version.logbackClassic,
    )

    val googleS2 = declare(
      "com.google.geometry" % "s2-geometry" % Version.googleS2,
    )

    val gtCrs = declare(
      "org.geotools" % "gt-main"  % Version.geoTools exclude ("javax.media", "jai_core"),
      "javax.media"  % "jai_core" % "1.1.3" from "https://repo.osgeo.org/repository/release/javax/media/jai_core/1.1.3/jai_core-1.1.3.jar",
    )
  }

  object Settings {
    val zioGrpc = Compile / PB.targets := Seq(
      scalapb.gen(grpc = true, scala3Sources = true) -> (Compile / sourceManaged).value / "scalapb",
      scalapb.zio_grpc.ZioCodeGenerator              -> (Compile / sourceManaged).value / "scalapb",
    )

    val zioTest = testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

    val scalamock = Test / scalacOptions += "-experimental"
  }

  val commonSettings: Seq[Setting[_]] = Seq(
    Settings.zioTest,
    Settings.scalamock,
  )
}
