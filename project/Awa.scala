import sbt._
import sbt.Keys._

object Awa {

  object Version {
    val awa    = "1.0.0"
    val scalaz = "7.3.8"
    val zio    = "2.1.17"
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
  }
}
