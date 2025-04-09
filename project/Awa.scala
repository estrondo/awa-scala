import sbt.*
import sbt.Keys.*
import sbtprotoc.ProtocPlugin.autoImport.PB

object Awa {

  object Version {
    val awa            = "1.0.0"
    val scalaz         = "7.3.8"
    val zio            = "2.1.17"
    val postgresDriver = "42.7.5"
    val protoQuill     = "4.8.6"
    val locationTech   = "1.20.0"
  }

  object Dependencies {

    private def declare(modules: ModuleID*) =
      libraryDependencies ++= modules

    val scalaz = declare(
      "org.scalaz" %% "scalaz-core" % Version.scalaz,
    )

    val zio = declare(
      "dev.zio" %% "zio"         % Version.zio,
      "dev.zio" %% "zio-streams" % Version.zio,
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
  }

  val zioGrpcSettings = Compile / PB.targets := Seq(
    scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value / "scalapb",
    scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb",
  )
}
