package awa.sbt

import java.io.File

class DockerEnginer(cwd: File) extends ContainerEnginer with ProcessHandler {


  override def buildImage(repository: String, tag: String): String = {
    runProcess(Seq("docker", "build", "-t", s"$repository:$tag", "--allow", "network.host", "."), cwd) match {
      case SuccessProcessOutcome(out, err) => ""
      case failed: FailedProcessOutcome    => throw failed.exception
    }
  }

  override def lookUpImage(imageName: String): Option[String] = {
    runProcess(Seq("docker", "image", "ls", "--format", "{{.ID}}", imageName), cwd) match {
      case SuccessProcessOutcome(out, _) => out.headOption
      case failed: FailedProcessOutcome  => throw failed.exception
    }
  }
}
