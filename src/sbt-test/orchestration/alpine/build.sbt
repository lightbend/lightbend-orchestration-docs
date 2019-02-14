// #alpine_example
import com.typesafe.sbt.packager.docker._

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.7"

lazy val root = (project in file("."))
  .enablePlugins(SbtReactiveAppPlugin)
  .settings(
    name := "alpine-packages",
    rpPackagingDockerCommmands := Vector(
      Cmd("RUN", "/sbin/apk", "add", "--no-cache", "bash", "coreutils", "shadow"))
  )
// #alpine_example
