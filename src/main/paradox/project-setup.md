# Project Setup

Currently, [sbt](http://www.scala-sbt.org/) is the only supported build tool. Maven support is planned and will land in a future release.

## SBT

To setup your project, include and enable the [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app) plugin.

### Prerequisites

* [sbt](http://www.scala-sbt.org/) 0.13.13 (or later), 1.0.3 (or later)
* [Docker](https://www.docker.com/) for building and publishing images.

### Supported Versions:

At this time, the following technologies are supported:

* Akka 2.5 or later
* Lagom 1.4 or later
* Play 2.6 or later

### Plugin Usage

1) Add the following to your project's `project/plugins.sbt` file to enable the plugin.

```scala
addSbtPlugin("com.lightbend.rp" % "sbt-reactive-app" % "0.5.1")
```

2) Next, you'll need to enable it on each subprojects you wish to build images for. For instance, enabling it on the `front-end` project requires the following edit to `build.sbt`:

```scala
val frontEnd = project("front-end").enablePlugins(SbtReactiveAppPlugin)
```

> Using Lagom? You'll want to enable the plugin on each of your *impl* projects. Don't enable it for the *api* projects.

You'll also need to enable the Service Locator, which is covered in the [Required Manual Configuration](project-configuration.html#required-manual-configuration) section.

> Refer to [Manual Configuration](project-configuration.html#manual-configuration) for other available settings.

3) Finally, ensure that the plugin was enabled correctly:

```bash
sbt update
```
