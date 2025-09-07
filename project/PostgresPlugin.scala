package awa.sbt

import sbt._
import sbt.Keys._
import sbt.internal.util.ManagedLogger
import sbt.plugins.JvmPlugin
import scala.collection.JavaConverters._

object PostgresPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = noTrigger

  object autoImport {
    val postgresBaseImage      = settingKey[String]("Postgres base image.")
    val postgresUsername       = settingKey[String]("Postgres username")
    val postgresPassword       = settingKey[String]("Postgres password")
    val postgresDatabase       = settingKey[String]("Postgres database")
    val postgresBuildImage     = taskKey[String]("Build a new Postgres image with applied migrations.")
    val postgresGenerate       = taskKey[Seq[File]]("Generate PostgresTestImage object.")
  }

  import autoImport.*

  override def projectSettings: Seq[Setting[_]] = inConfig(Test)(
    Seq(
      postgresBaseImage      := "docker.io/postgres:17-alpine",
      postgresUsername       := "awa",
      postgresPassword       := "awa",
      postgresDatabase       := "awa",
      postgresBuildImage     := {
        val ctx = BuildContext(
          image = postgresBaseImage.value,
          database = postgresDatabase.value,
          username = postgresUsername.value,
          password = postgresPassword.value,
          logger = streams.value.log,
          projectDirectory = (ThisBuild / baseDirectory).value / "project",
          targetDirectory = IOF.createDirectory(target.value / "postgres-image-stage"),
        )

        new ImageBuilder(ctx)
          .build(
            baseDirectory.value / "src" / "main" / "resources" / "db" / "migration",
            new DockerEnginer(ctx.targetDirectory)
          )
      },
      postgresGenerate := {
        val imageName = postgresBuildImage.value
        val source = s"""package awa.sbt
        |// Generated
        |object AwaPostgreSQLTest {
        |  val image: String = "$imageName"
        |}
        """.stripMargin

        val directory = IOF.createDirectory(managedSourceDirectories.value.head / "awa" / "sbt")
        val generated = directory / "AwaPostgreSQLTest.scala"
        IO.write(generated, source)
        Seq(generated)
      },
      sourceGenerators += postgresGenerate,
      compile                := {
        compile.value
      },
    ),
  )
}
