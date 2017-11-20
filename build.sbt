lazy val root = (project in file("."))
  .enablePlugins(ParadoxPlugin)
  .settings(
    organization := "com.lightbend",
            name := "reactive-platform-tooling-docs",
         version := "",
    resolvers += Resolver.bintrayRepo("typesafe", "internal-maven-releases"),
    paradoxTheme := Some("com.lightbend.paradox" % "paradox-theme-lightbend" % "0.2.3")
  )
