import Awa._

ThisBuild / scalaVersion := "3.6.4"
ThisBuild / organization := "one.estrondo"
ThisBuild / version      := Version.awa

ThisBuild / scalacOptions ++= Seq(
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "awa-root",
  )
  .aggregate(
    core,
    zioUtil,
  )

lazy val core = project
  .in(file("core"))
  .settings(
    name := "awa-core",
    Dependencies.scalaz,
    Dependencies.zio,
  )

lazy val zioUtil = project
  .in(file("zio/util"))
  .settings(
    name := "awa-zio",
    Dependencies.zio,
    Dependencies.scalaz,
  )
