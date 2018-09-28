// #slim_example
import com.typesafe.sbt.packager.docker._

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.7"

lazy val root = (project in file("."))
  .enablePlugins(SbtReactiveAppPlugin)
  .settings(
    name := "debian-packages",
    dockerBaseImage := "openjdk:8-jdk-slim",
    // override the apk packaging
    rpPackagingDockerCommmands := Vector(),

    // optionally add apt-get packages
    // rpPackagingDockerCommmands := Vector(
    //   Cmd("RUN", "/usr/bin/apt-get", "install", "-y", "bash")),
  )
// #slim_example
