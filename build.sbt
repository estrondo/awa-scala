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
    core,
    zioCore,
    persistencePostgres,
    liveTrackingGrpcZIO,
    accountGrpcZIO,
  )

lazy val core = (project in file("core"))
  .settings(
    name := "awa-core",
    Dependencies.locationTech,
  )

lazy val zioCore = (project in file("zio/core"))
  .settings(
    name := "awa-zio-core",
    Dependencies.zio,
  )
  .dependsOn(
    core % cctt,
  )

lazy val persistencePostgres = (project in file("persistence/postgres"))
  .settings(
    name := "awa-persistence-postgres",
    Dependencies.postgresDriver,
    Dependencies.protoQuill,
    Dependencies.scalaz,
  )
  .dependsOn(
    core    % cctt,
    zioCore % cctt,
  )

lazy val service = (project in file("service"))
  .settings(
    name := "awa-service-impl",
    Dependencies.scalaz,
  )
  .dependsOn(
    core    % cctt,
    zioCore % cctt,
  )

lazy val accountGrpcZIO = (project in file("zio/account"))
  .settings(
    name := "awa-account-grpc-zio",
    Settings.zioGrpc,
    Dependencies.zioGrpc,
  )
  .dependsOn(
    core    % cctt,
    zioCore % cctt,
  )

lazy val liveTrackingGrpcZIO = (project in file("zio/live-tracking"))
  .settings(
    name := "awa-live-tracking-grpc-zio",
    Dependencies.zioGrpc,
    Settings.zioGrpc,
  )
  .dependsOn(
    core    % cctt,
    zioCore % cctt,
  )
