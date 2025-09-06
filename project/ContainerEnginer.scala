package awa.sbt

trait ContainerEnginer {

  def buildImage(
    repository: String,
    tag: String,
  ): String
  
  // It returns the imageId
  def lookUpImage(imageName: String): Option[String]
}
