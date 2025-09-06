addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.14.3")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")

libraryDependencies +=
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.6.3"

addSbtPlugin("com.github.sbt" % "sbt-dynver" % "5.1.0")

libraryDependencies ++= Seq(
  "org.flywaydb"           % "flyway-core"                       % "11.11.2",
  "org.flywaydb"           % "flyway-database-postgresql"        % "11.11.2",
  "org.postgresql"         % "postgresql"                        % "42.7.7",
)
