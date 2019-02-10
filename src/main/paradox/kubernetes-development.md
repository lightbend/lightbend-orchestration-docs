# Kubernetes Development

[sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app) includes an sbt task, `rpDeploy minikube`, that can be
used to simplify running your application in your local Minikube. This is especially useful as a development tool
to get a quick feedback on modifications as you develop your applications.

It's integrated with Lightbend's [Reactive Sandbox](https://github.com/lightbend/reactive-sandbox) which provides
development-grade (i.e. don't use it in production) installations of Cassandra, Elasticsearch, Kafka, and ZooKeeper.

### Prerequisites

* An sbt project with [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app) set up
* [reactive-cli](https://github.com/lightbend/reactive-cli) installed
* [helm](https://helm.sh/)
* [minikube](https://github.com/kubernetes/minikube) v0.25.0 or later
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

### Getting Started

To use this feature, launch the sbt console and type `rpDeploy minikube`. This will run the task on all aggregated
subprojects. The task will install Helm and the Reactive Sandbox (if required) and install (or replace) all of the
applications. You can also run the task on a specific subproject if you only wish to deploy that application.

> You can combine this with sbt's `~` operator to run the task on every file change.

For example, see the following (trimmed) output below:

```bash
sbt 'rpDeploy minikube'
[info] Loading global plugins from /home/longshorej/.sbt/0.13/plugins
[info] Loading project definition from /home/longshorej/work/lightbend/online-auction-scala/project
[info] Set current project to online-auction-scala (in build file:/home/longshorej/work/lightbend/online-auction-scala/)

...
[info] Built image biddingimpl:1.0.0-SNAPSHOT
[info] Built image userimpl:1.0.0-SNAPSHOT
[info] Built image itemimpl:1.0.0-SNAPSHOT
[info] Built image searchimpl:1.0.0-SNAPSHOT
[info] deployment "bidding-v1-0-0-snapshot" deleted
[info] service "bidding" deleted
[info] deployment "bidding-v1-0-0-snapshot" created
[info] service "bidding" created
[info] deployment "user-v1-0-0-snapshot" deleted
[info] deployment "item-v1-0-0-snapshot" deleted
[info] service "user" deleted
[info] service "item" deleted
[info] deployment "user-v1-0-0-snapshot" created
[info] deployment "item-v1-0-0-snapshot" created
[info] service "user" created
[info] service "item" created
[info] deployment "search-v1-0-0-snapshot" deleted
[info] service "search" deleted
[info] ingress "search" deleted
[info] deployment "search-v1-0-0-snapshot" created
[info] service "search" created

```

### Configuration

The following sbt keys can be used to configure this feature, e.g. to register additional external services, control
the Reactive Sandbox installation, and define additional `rp` arguments.

| Name / Type                                                                      | Description                                           |
|----------------------------------------------------------------------------------|-------------------------------------------------------|
| rpDeployMinikubeReactiveSandboxExternalServices<br/><br/> `Map[String, String]`    | A map of service names to service lookup addresses. This will be provided as an argument to rp for resources that are generated when running deploy minikube. Note that this map will only be added if reactive sandbox is enabled.
| rpDeployMinikubeAdditionalExternalServices <br/><br/> `Map[String, String]`        | An additional map of service names to service lookup addresses. These will always be provided to rp and take precedence over the Reactive Sandbox addresses.
| rpDeployMinikubeAkkaClusterBootstrapContactPoints               <br/><br/> `Int`   | When deploying applications with Akka Cluster Bootstrap enabled, the services will initially be started with this many contact points / replicas. Defaults to 1
| rpDeployMinikubeEnableReactiveSandbox      <br/><br/> `Boolean`                    | If enabled, Reactive Sandbox (a Docker image containing Cassandra, Kafka, ZooKeeper, Elasticsearch) will be deployed with this app.
| rpDeployMinikubePlayHostAllowedProperty              <br/><br/> `String`           | If deploying a Play application, this property will be set to the Minikube IP.
| rpDeployMinikubePlayHttpSecretKeyProperty                  <br/><br/> `String`     | If deploying a Play application, this property will be set to the value specified below.
| rpDeployMinikubePlayHttpSecretKeyValue       <br/><br/> `String`                   | If deploying a Play application, this property will be set to the value specified above.
| rpDeployMinikubeReactiveSandboxCqlStatements           <br/><br/> `Seq[String]`    | Set this setting (build-wide, i.e. `deployMinikubeReactiveSandboxCqlStatements in ThisBuild` := ...) to a sequence of CQL statements that should be executed against Cassandra when the Reactive Sandbox is installed.
| rpDeployMinikubeRpArguments           <br/><br/> `Seq[String]`                     | Additional arguments to invoke rp with for this app.
