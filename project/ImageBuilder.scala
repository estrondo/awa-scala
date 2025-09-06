package awa.sbt

import java.io.File
import sbt.io.IO
import sbt._
import java.nio.charset.StandardCharsets

class ImageBuilder(ctx: BuildContext) extends LoggerHelper {

  private implicit def _ctx = ctx

  def build(location: File, enginer: ContainerEnginer): String = {
    val expectedTag = Migration.computeTag(location)
    val expectedImageName = fullImageName(expectedTag)
    enginer.lookUpImage(expectedImageName) match {
      case Some(imageId) => 
        logger.info(s"Image found: $imageId.")
      case None =>
        logger.info(s"Image $expectedImageName was not found.")
        val port = Network.getRandomPort()
        IO.write(ctx.targetDirectory / "pg-port", port.toString(), StandardCharsets.UTF_8)
        IO.write(ctx.targetDirectory / "Dockerfile", generateDockerfile(), StandardCharsets.UTF_8)
        IO.copyFile(ctx.projectDirectory / "pg-start.sh", ctx.targetDirectory / "pg-start.sh")

        val future = Migration.start(location, port)

        enginer.buildImage(
          "localhost/awa-postgres",
          expectedTag
        )

        future.get()
    }

    expectedImageName
  }

  private def fullImageName(tag: String) = s"localhost/awa-postgres:$tag"

  private def generateDockerfile(): String = {
    IO.read(ctx.projectDirectory / "Dockerfile")
      .replaceAllLiterally("{BASE}", ctx.image)
      .replaceAllLiterally("{POSTGRES_DB}", ctx.database)
      .replaceAllLiterally("{POSTGRES_USER}", ctx.username)
      .replaceAllLiterally("{POSTGRES_PASSWORD}", ctx.password)
  }
}
