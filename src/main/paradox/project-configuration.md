# Project Configuration

The SBT plugin, [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app), inspects the project it is included in to provide a set of reasonable defaults and reduce the amount of configuration required to a bare minimum. Refer to the sections below to understand what features are available, which are automatically enabled, and when manual configuration is required.

## Automatic Configuration

Refer to the listing below to understand what functionality is provided automatically by the plugin for your framework.

> Refer to the *Manual Configuration* section below for information on how you can manually enable and disable these for all applications.

### Lagom

* Akka Cluster Formation
* Docker & JVM Configuration
* Endpoint Detection
* Port Assignment & Binding
* Service Locator
* Status

### Play

* Docker & JVM Configuration
* Port Assignment & Binding

### JVM

* Docker & JVM Configuration

## Manual Configuration

Your application may require that you manually enable or disable the various settings provided by `sbt-reactive-app`. Refer to the table below to understand what settings are available. 

### SBT Settings & Tasks

| Name / Type                                                              | Description                                           |
|--------------------------------------------------------------------------|-------------------------------------------------------|
| appName                    <br/><br/> `Option[String]`                   | Specifies the service name. Defaults to the SBT project's name for regular projects. Defaults to the Lagom service name for Lagom projects |
| enableAkkaClusterBootstrap <br/><br/> `Option[Boolean]`                  | Specifies whether Akka Cluster Bootstrapping should be enabled. When enabled, an Akka extension will be enabled that will automatically form your cluster using service discovery.  |
| enableCommon               <br/><br/> `Boolean`                          | Specifies whether basic features like Platform detection should be enabled |
| enablePlayHttpBinding      <br/><br/> `Boolean`                          | Specifies whether automatic HTTP port binding for Play & Lagom should be enabled|
| enableSecrets              <br/><br/> `Option[Boolean]`                  | Specifies whether secrets library should be enabled |
| endpoints                  <br/><br/> `Seq[Endpoint]`                    | Declare the endpoints that should be made available for your service |
| environmentVariables       <br/><br/> `Map[String, EnvironmentVariable]` | Declare values that should be bound to environment variables (application runtime). Note that additional environment variables can also be set during deploy time using the `rp` command. |
| httpIngressHosts           <br/><br/> `Seq[String]`                      | For automatic HTTP ingress declarations, specifies the host used for ingress. |
| httpIngressPorts           <br/><br/> `Seq[Int]`                         | For automatic HTTP ingress declarations, specifies the port used for ingress. |
| prependRpConf              <br/><br/> `Option[String]`                   | All configuration files on the class path with this name will be prepended to the applications `application.conf`. This is the mechanism used to automatically configure various dependencies. To disable this, set this setting to `None` |
| startScriptLocation        <br/><br/> `Option[String]`                   | A custom start-script is provided and bundles with the application. Change its location with this setting. |

### SBT Native Packager

Under the hood, the tooling uses [SBT Native Packager](https://github.com/sbt/sbt-native-packager) to create Docker images. Under most circumstances, the defaults are sufficient. However, for advanced and complex scenarios, be sure to consult its documentation for additional Docker-related settings.