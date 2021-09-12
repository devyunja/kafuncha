import com.typesafe.sbt.packager.docker.DockerChmodType

name := """kafuncha-backend"""
organization := "coffee.programming"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.14"

val scalaTestVersion = "5.0.0"
val sparkVersion = "3.1.2"
val postgresqlDriverVersion = "42.2.23"
val AwsSdkVersion = "1.11.1018"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestVersion % Test,
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.postgresql" % "postgresql" % postgresqlDriverVersion,
  "com.amazonaws" % "aws-java-sdk" % AwsSdkVersion
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "coffee.programming.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "coffee.programming.binders._"

// MacOS m1 support
PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.jdk7(play.sbt.run.toLoggerProxy(sLog.value))

/**
 * Docker Settings
 */

dockerBaseImage := "openjdk:11"
dockerExposedPorts ++= Seq(9000)

// Point the Play logs at the right place.
Docker / defaultLinuxLogsLocation := (Docker / defaultLinuxInstallLocation).value + "/logs"
dockerExposedVolumes := Seq((Docker / defaultLinuxLogsLocation).value)

// Don't let Docker write out a PID file to /opt/docker, there's no write access,
// and it doesn't matter anyway.
dockerEnvVars := Map(
  "JAVA_TOOL_OPTIONS" -> "-Dpidfile.path=/dev/null",
  "LOG_DIR" -> (Docker / defaultLinuxLogsLocation).value
)

dockerChmodType := DockerChmodType.UserGroupWriteExecute
