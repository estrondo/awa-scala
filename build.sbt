import Awa._

ThisBuild / scalaVersion    := "3.6.4"
ThisBuild / organization    := "one.estrondo"
ThisBuild / dynverSeparator := "_"

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
    `module-service-impl`,
    `module-persistence-postgres`,
    `module-account-grpc`,
    `module-live-track-grpc`,
    `shared-ducktape`,
    `shared-logging`,
    `shared-grpc`,
    `shared-gt-crs`,
    `shared-kafka`,
    `shared-test-core`,
    `shared-test-scalamock`,
    `shared-test-testcontainers`,
    `generated-live-track-grpc`,
  )

lazy val `core` = (project in file("core"))
  .settings(
    name := "awa-core",
    Dependencies.zio,
    Dependencies.locationTech,
    Dependencies.ducktape,
    Settings.zioTest,
  )

lazy val `shared-grpc` = (project in file("shared/grpc"))
  .settings(
    name := "awa-shared-grpc",
    Dependencies.zioGrpc,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-logging` = (project in file("shared/logging"))
  .settings(
    name := "awa-logging",
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-ducktape` = (project in file("shared/ducktape"))
  .settings(
    name := "awa-shared-ducktape",
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-gt-crs` = (project in file("shared/gt-crs"))
  .settings(
    name := "awa-shared-gt-crs",
    Repositories.osgeo,
    Dependencies.gtCrs,
  )
  .dependsOn(
    core % cctt,
  )

lazy val `shared-kafka` = (project in file("shared/kafka"))
  .settings(
    name := "awa-shared-kafka",
    Dependencies.avro4s,
    Dependencies.kafka,
    Settings.zioTest,
  )
  .dependsOn(
    core                         % cctt,
    `shared-test-core`           % Test,
    `shared-test-testcontainers` % Test,
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
    Dependencies.testContainers,
    Dependencies.protoQuill,
    Dependencies.postgresDriver,
    Dependencies.kafka,
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
    `shared-ducktape`            % cctt,
    `shared-test-core`           % Test,
    `shared-test-testcontainers` % Test,
    `shared-test-scalamock`      % Test,
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
    `core`            % cctt,
    `shared-grpc`     % cctt,
    `shared-ducktape` % cctt,
  )

lazy val `module-live-track-grpc` = (project in file("modules/live-track-grpc"))
  .settings(
    name := "awa-live-track-grpc",
  )
  .dependsOn(
    `core`                      % cctt,
    `generated-live-track-grpc` % cctt,
    `shared-grpc`               % cctt,
    `shared-ducktape`           % cctt,
    `shared-gt-crs`             % cctt,
    `shared-test-core`          % Test,
    `shared-test-scalamock`     % Test,
  )

lazy val `generated-live-track-grpc` = (project in file("generated/live-track-grpc"))
  .settings(
    name := "awa-generated-live-track-grpc",
    Dependencies.zioGrpc,
    Settings.zioGrpc,
  )
  .dependsOn(
    `shared-test-core` % Test,
  )
