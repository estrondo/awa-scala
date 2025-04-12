import Awa.*

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
    `shared-logging`,
    `shared-scalamock`,
    `module-service-impl`,
    `module-persistence-postgres`,
    `module-account-grpc`,
    `module-live-tracking-grpc`,
  )

lazy val `core` = (project in file("core"))
  .settings(
    name := "awa-core",
    Dependencies.zio,
    Dependencies.locationTech,
    Dependencies.ducktapeTest,
    Settings.zioTest,
  )

lazy val `shared-logging` = (project in file("shared/logging"))
  .settings(
    name := "awa-logging",
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-scalamock` = (project in file("shared/scala-mock"))
  .settings(
    name := "awa-shared-scalamock",
    Dependencies.zio,
    Dependencies.scalamockWithCompile,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `module-persistence-postgres` = (project in file("modules/persistence-postgres"))
  .settings(
    name := "awa-persistence-postgres",
    Dependencies.postgresDriver,
    Dependencies.protoQuill,
  )
  .dependsOn(
    `core` % cctt,
  )

lazy val `module-service-impl` = (project in file("modules/service-impl"))
  .settings(
    name := "awa-service-impl",
    Dependencies.ducktape,
    Dependencies.scalaMock,
    Settings.scalamock,
  )
  .dependsOn(
    `core`             % cctt,
    `shared-logging`   % cctt,
    `shared-scalamock` % Test,
  )

lazy val `module-account-grpc` = (project in file("modules/account-grpc"))
  .settings(
    name := "awa-account-grpc",
    Settings.zioGrpc,
    Dependencies.zioGrpc,
  )
  .dependsOn(
    `core` % cctt,
  )

lazy val `module-live-tracking-grpc` = (project in file("modules/live-tracking-grpc"))
  .settings(
    name := "awa-live-tracking-grpc",
    Dependencies.zioGrpc,
    Settings.zioGrpc,
  )
  .dependsOn(
    `core` % cctt,
  )
