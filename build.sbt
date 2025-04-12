import Awa._

ThisBuild / scalaVersion := "3.6.4"
ThisBuild / organization := "one.estrondo"
ThisBuild / version      := Version.awa

ThisBuild / scalacOptions ++= Seq(
  "-Wunused:all",
  "-Xsemanticdb",
)

//noinspection SpellCheckingInspection
val cctt = "compile->compile;test->test"

lazy val root = project
  .in(file("."))
  .settings(
    name := "awa",
  )
  .aggregate(
    `core`,
    `shared-zio-core`,
    `module-service-impl`,
    `module-persistence-postgres`,
    `module-live-tracking-grpc-zio`,
    `module-account-grpc-zio`,
  )

lazy val `core` = (project in file("core"))
  .settings(
    name := "awa-core",
    Dependencies.locationTech,
  )

lazy val `shared-zio-core` = (project in file("shared/zio"))
  .settings(
    name := "awa-zio",
    Dependencies.zio,
  )
  .dependsOn(
    `core` % cctt,
  )

lazy val `module-persistence-postgres` = (project in file("modules/persistence-postgres"))
  .settings(
    name := "awa-persistence-postgres",
    Dependencies.postgresDriver,
    Dependencies.protoQuill,
    Dependencies.scalaz,
  )
  .dependsOn(
    `core`            % cctt,
    `shared-zio-core` % cctt,
  )

lazy val `module-service-impl` = (project in file("modules/service-impl"))
  .settings(
    name := "awa-service-impl",
    Dependencies.scalaz,
  )
  .dependsOn(
    `core`            % cctt,
    `shared-zio-core` % cctt,
  )

lazy val `module-account-grpc-zio` = (project in file("modules/account-grpc-zio"))
  .settings(
    name := "awa-account-grpc-zio",
    Settings.zioGrpc,
    Dependencies.zioGrpc,
  )
  .dependsOn(
    `core`            % cctt,
    `shared-zio-core` % cctt,
  )

lazy val `module-live-tracking-grpc-zio` = (project in file("modules/live-tracking-grpc-zio"))
  .settings(
    name := "awa-live-tracking-grpc-zio",
    Dependencies.zioGrpc,
    Settings.zioGrpc,
  )
  .dependsOn(
    `core`            % cctt,
    `shared-zio-core` % cctt,
  )
