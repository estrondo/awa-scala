package awa.sbt

import sbt._
import sbt.Keys._
import sbt.util.Logger

case class BuildContext(
  image: String,
  database: String,
  username: String,
  password: String,
  logger: Logger,
  projectDirectory: File,
  targetDirectory: File,
)
