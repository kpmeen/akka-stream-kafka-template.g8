name := "$project_name$"
organization := "$organization$"
scalaVersion := "$scala_version$"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.18"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage := "openjdk:8"
dockerRepository := Some("$docker_repository$")
