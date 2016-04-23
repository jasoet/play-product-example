name := """play-product-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala,DockerPlugin)

scalaVersion := "2.11.8"

maintainer := "Deny Prasetyo"
dockerRepository := Some("jasoet")
dockerUpdateLatest := true
daemonUser in Docker := "root"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play" %% "anorm" % "2.5.1",
  "org.flywaydb" %% "flyway-play" % "3.0.0",
  "org.postgresql" % "postgresql" % "9.4.1208",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
