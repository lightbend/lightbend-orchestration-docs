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

## Example Project

Before explaining the configuration in detail, let's take a look at steps needed to use platform tooling with an existing project. We've provided an example Lagom application: [lagom-java-chirper-tooling-example](https://github.com/mitkus/lagom-java-chirper-tooling-example). It is a small Twitter clone utilising microservice architecture and event sourcing.

All that's needed to make it work with Reactive Platform Tooling is to add `sbt-reactive-app` plugin by putting this in `project/plugins.sbt`:

```scala
addSbtPlugin("com.lightbend.rp" % "sbt-reactive-app" % "0.6.0")
```

Then, enable it on each subproject which will get packaged into a Docker image. When using Lagom those are service *impl* projects, also any frontends that you have. Don't enable it for the *api* projects, those only define service interfaces and do not produce any executables. In our case this can be done by adding `SbtReactiveAppPlugin` to the `enablePlugins()` call in `build.sbt` file. It should look like this:

```scala
lazy val friendImpl = project("friend-impl")
    .enablePlugins(LagomJava, SbtReactiveAppPlugin)
...
```

> Refer to [Manual Configuration](project-configuration.html#manual-configuration) for other available settings.

Besides `friend-impl` shown above, enable the plugin for `chirp-impl`, `activity-stream-impl`, `load-test-impl` and `front-end` projects.

Next step is neccessary for Lagom Java services to find each other when running on Kubernetes. We need to enable service locator module by adding a line to `friend-impl/src/main/resources/application.conf`:

```hocon
play.modules.enabled += "com.lightbend.rp.servicediscovery.lagom.javadsl.ServiceLocatorModule"
```

Again, we need to repeat it for all service subprojects, so make sure to also add this line to `application.conf` files in `chirp-impl`, `activity-stream-impl`, `load-test-impl` and `front-end`. Now you're ready to check if plugin is set up correctly! Try running this in your terminal:

```bash
sbt update
```

If everything went well, now you can build Docker images and publish them to your local registry:

```bash
sbt docker:publishLocal
docker images
```

Last command will show installed images, `friend-impl:1.0.0-SNAPSHOT` and others should be among them. Once you're here, you can generate Kuberenetes resources for deploying these images using [`reactive-cli`](kubernetes-deployment.html) tool. However, we can automate that to reduce your iteration times during development. Assuming you have [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) and `reactive-cli` installed, just run this:

```bash
minikube start --memory 6000
sbt "deploy minikube"
echo "http://$(minikube ip)"
```

Now you should be able to enter URL printed out by echo in your browser and try out Chirper application. Don't worry if that doesn't work yet, we'll explain how to setup `Minikube` and `reactive-cli` in other sections.