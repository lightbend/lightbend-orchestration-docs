lazy val root = (project in file("."))
  .enablePlugins(ParadoxPlugin, ParadoxSitePlugin)
  .settings(
    organization := "com.lightbend",
            name := "lightbend-orchestration-kubernetes-docs",
         version := "",
    resolvers += Resolver.bintrayRepo("typesafe", "internal-maven-releases"),
    sourceDirectory in Paradox := sourceDirectory.value / "main" / "paradox",
    sourceDirectory in (Paradox, paradoxTheme) := sourceDirectory.value / "main" / "paradox" / "_template",
    paradoxProperties in Compile ++= Map(
      "sbtreactiveapp" -> "1.3.0",
      "reactivelib"    -> "0.9.0",
      "reactivecli"    -> "1.3.1"
    ),
    paradoxProperties in Paradox := (paradoxProperties in Compile).value,
    previewFixedPort := Some(8000),
    run := {
      previewSite.value
    }
  )
