# Project Configuration

The sbt plugin, [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app), inspects the project it is included
in to provide a set of reasonable defaults and reduce the amount of configuration required to a bare minimum. Refer to
the sections below to understand what features are available, which are automatically enabled, and when manual configuration is required.

## Automatic Configuration

Refer to the listing below to understand what functionality is provided automatically by the plugin for your framework.

> Refer to the *Additional Configuration* section below for information on how you can manually enable and disable these for all applications.

### Lagom

* Akka Cluster Formation
* Docker & JVM Configuration
* Endpoint Detection
* Port Assignment & Binding
* Service Locator <sup>1</sup>
* Status

### Play

* Docker & JVM Configuration
* Port Assignment & Binding

<sup>1</sup> Note that the Lagom service locator must be manually enabled (Java) or mixed into your application (Scala).*

### JVM

* Docker & JVM Configuration

## Required Configuration

### Service Locator

[reactive-lib](https://github.com/lightbend/reactive-lib/), which is automatically included by the sbt plugin, provides a service
locator that can be used by JVM applications to easily locate other services using DNS SRV. Additionally, the project
provides an implementation of Lagom's service locator. Use the configuration below to enable the Lagom service locator.

#### Lagom Java

Add the following configuration to your `application.conf` file to enable the Lagom Java Service Locator:

```hocon
play.modules.enabled +=
  "com.lightbend.rp.servicediscovery.lagom.javadsl.ServiceLocatorModule"
```

#### Lagom Scala

When declaring your Lagom application, for each service you will need to mix in the `com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents` trait:

```scala
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents

...

class LagomLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) =
    new MyLagomApplication(context) with LagomServiceLocatorComponents

...
```

## Recommended Configuration

### Split Brain Resolver

If your application uses Akka Cluster (which includes most Lagom services), you'll need a solution to deal with downing of rescheduled cluster members
as well as a strategy for dealing with network partitions. Lightbend provides a solution to its customers as part of
the [Lightbend Reactive Platform](http://www.lightbend.com/products/lightbend-reactive-platform).

Be sure to consult the documentation on [Split Brain Resolver](https://developer.lightbend.com/docs/akka-commercial-addons/current/split-brain-resolver.html)
to learn how to configure your application to use it.

## Additional Configuration

Your application may require that you manually enable or disable the various settings provided by `sbt-reactive-app`. Refer to the table below to understand what settings are available.

### sbt Settings & Tasks

| Name / Type                                                              | Description                                           |
|--------------------------------------------------------------------------|-------------------------------------------------------|
| appName                    <br/><br/> `String`                           | Specifies the service name. Defaults to the sbt project's name for regular projects. Defaults to the Lagom service name for Lagom projects |
| annotations                <br/><br/> `Map[String, String]`              | Key/value pairs to export as annotations. |
| enableAkkaClusterBootstrap <br/><br/> `Boolean`                          | Specifies whether Akka Cluster Bootstrapping should be enabled. When enabled, an Akka extension will be enabled that will automatically form your cluster using service discovery.  |
| enableCommon               <br/><br/> `Boolean`                          | Specifies whether basic features like Platform detection should be enabled |
| enablePlayHttpBinding      <br/><br/> `Boolean`                          | Specifies whether automatic HTTP port binding for Play & Lagom should be enabled|
| enableSecrets              <br/><br/> `Boolean`                          | Specifies whether secrets library should be enabled |
| endpoints                  <br/><br/> `Seq[Endpoint]`                    | Declare the endpoints that should be made available for your service |
| environmentVariables       <br/><br/> `Map[String, EnvironmentVariable]` | Declare values that should be bound to environment variables (application runtime). Note that additional environment variables can also be set during deploy time using the `rp` command. |
| httpIngressHosts           <br/><br/> `Seq[String]`                      | For automatic HTTP ingress declarations, specifies the host used for ingress. |
| httpIngressPorts           <br/><br/> `Seq[Int]`                         | For automatic HTTP ingress declarations, specifies the port used for ingress. |
| prependRpConf              <br/><br/> `String`                           | All configuration files on the class path with this name will be prepended to the applications `application.conf`. This is the mechanism used to automatically configure various dependencies. To disable this, set this setting to `None` |
| startScriptLocation        <br/><br/> `String`                           | A custom start-script is provided and bundles with the application. Change its location with this setting. |

### sbt Native Packager

Under the hood, the tooling uses [sbt Native Packager](https://github.com/sbt/sbt-native-packager) to create Docker images. Under most circumstances, the defaults are sufficient. However, for advanced and complex scenarios, be sure to consult its documentation for additional Docker-related settings.
