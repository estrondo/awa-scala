import sbt.*
import sbt.Keys.*
import sbtprotoc.ProtocPlugin.autoImport.PB

object Awa {

  object Version {
    val awa            = "1.0.0"
    val zio            = "2.1.17"
    val postgresDriver = "42.7.5"
    val protoQuill     = "4.8.6"
    val locationTech   = "1.20.0"
    val ducktape       = "0.2.8"
    val scalaMock      = "7.3.0"
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
      "io.grpc"               % "grpc-netty"           % "1.71.0",
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
  }

  object Settings {
    val zioGrpc = Compile / PB.targets := Seq(
      scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value / "scalapb",
      scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb",
    )

    val zioTest = testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

    val scalamock = Test / scalacOptions += "-experimental"
  }
}
