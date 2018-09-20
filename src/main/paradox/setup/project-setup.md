# Project Setup

Currently, [sbt](http://www.scala-sbt.org/) is the only supported build tool. Maven support is planned and will land in a future release.

## sbt

To setup your project, include and enable the [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app) plugin.

### Prerequisites

* [sbt](http://www.scala-sbt.org/) 0.13.13 (or later), 1.0.3 (or later)
* [Docker](https://www.docker.com/) for building and publishing images.

### Supported Versions:

At this time, the following technologies are supported:

* Akka 2.5 or later
* Lagom 1.4 or later
* Play 2.6 or later

### Example Project

Before explaining the configuration in detail, let's take a look at steps needed to use Lightbend Orchestration in an existing project. We've provided an example Lagom application: [lagom-java-chirper-tooling-example](https://github.com/mitkus/lagom-java-chirper-tooling-example). It's a small Twitter clone utilising a microservices architecture and event sourcing.

#### Setup

To use it with Lightbend Orchestration, follow these steps:

1) Add the plugin to your project's `project/plugins.sbt` file:

@@@vars
```scala
addSbtPlugin("com.lightbend.rp" % "sbt-reactive-app" % "$sbtreactiveapp$")
```
@@@

2) Enable it for each subproject which will get packaged into a Docker image. When using Lagom those are service *impl* projects, as well as any frontends that you have. Don't enable it for the *api* projects, those only define service interfaces and do not produce any executables. In our case this can be done by adding `SbtReactiveAppPlugin` to the `enablePlugins()` call in `build.sbt` file. It should look like this:

```scala
lazy val friendImpl = project("friend-impl")
    .enablePlugins(LagomJava, SbtReactiveAppPlugin)
...
```

> Refer to [Manual Configuration](project-configuration.html#manual-configuration) for other available settings.

In addition to `friend-impl` shown above, enable the plugin for `chirp-impl`, `activity-stream-impl`, `load-test-impl` and `front-end` projects.

3) Enable the service locator module by adding a line to `friend-impl/src/main/resources/application.conf`:

```hocon
play.modules.enabled += "com.lightbend.rp.servicediscovery.lagom.javadsl.ServiceLocatorModule"
```

Again, we need to repeat it for all service subprojects, so make sure to also add this line to `application.conf` files in `chirp-impl`, `activity-stream-impl`, `load-test-impl` and `front-end`. Now you're ready to check if plugin is set up correctly! Try running this in your terminal:

```bash
sbt update
```

#### Build

Now that your project is setup, you can use sbt to build Docker images and publish them to your local registry:

```bash
sbt docker:publishLocal
docker images
```

Later in the documentation we'll cover how you can then generate Kubernetes and DC/OS configuration from these Docker images.

#### Kubernetes Developer Deployment

> The following feature is currently only available for Kubernetes.

Lightbend Orchestration provides an sbt task, `deploy minikube`, that makes it easy for developers to deploy their application to their own local Kubernetes cluster.

The following command will build and deploy all of your services into your local Kubernetes cluster. You'll need to ensure that the following software is installed:

* [Docker](https://www.docker.com/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl)
* [Minikube](https://github.com/kubernetes/minikube) v0.25.0 or later (Verify with `minikube version`)
* [Helm](https://github.com/kubernetes/helm)
* [reactive-cli](cli-installation.html#install-the-cli) 1.1.0 or later (Verify with `rp version`)

After installing the requisite software, run the following commands:

```bash
minikube start --memory 6000
sbt "deploy minikube"
echo "http://$(minikube ip)"
```

Now you should be able to open the printed URL in your browser and try out the Chirper application. Later in the documentation we'll cover how you can use these same tools in a production setting, covering deployment to both Kubernetes and DC/OS.
