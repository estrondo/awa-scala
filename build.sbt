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
    `shared-logging`,
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
    Dependencies.ducktape,
    Settings.zioTest,
  )

lazy val `shared-logging` = (project in file("shared/logging"))
  .settings(
    name := "awa-logging",
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-test-core` = (project in file("shared-test/core"))
  .settings(
    name := "awa-shared-test-core",
    Dependencies.ducktape,
    Dependencies.scalamockWithCompile,
    Dependencies.zioTestWithCompile,
    Dependencies.logging,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-test-scalamock` = (project in file("shared-test/scalamock"))
  .settings(
    name := "awa-shared-test-scalamock",
    Dependencies.zio,
    Dependencies.scalamockWithCompile,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-test-testcontainers` = (project in file("shared-test/testcontainers"))
  .settings(
    name := "awa-shared-test-testcontainers",
    Dependencies.testcontainers,
    Dependencies.protoQuill,
    Dependencies.postgresDriver,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `module-persistence-postgres` = (project in file("modules/persistence-postgres"))
  .settings(
    name := "awa-persistence-postgres",
    Dependencies.postgresDriver,
    Dependencies.protoQuill,
    Dependencies.ducktape,
    Dependencies.logging,
  )
  .dependsOn(
    `core`                       % cctt,
    `shared-test-core`           % Test,
    `shared-test-testcontainers` % Test,
  )

lazy val `module-service-impl` = (project in file("modules/service-impl"))
  .settings(
    name := "awa-service-impl",
    Dependencies.ducktape,
    Dependencies.scalaMock,
    Dependencies.logging,
    Settings.scalamock,
  )
  .dependsOn(
    `core`                  % cctt,
    `shared-logging`        % cctt,
    `shared-test-scalamock` % Test,
    `shared-test-core`      % Test,
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
