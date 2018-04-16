# Features Reference

## Akka Cluster Formation

When enabled, this feature will use service discovery to identify other contact points. Once the requisite number of
contact points have been identified, the one with the lowest address will form the cluster. The other nodes will then join that one.

This feature is automatically enabled for Lagom applications that require it. It can be manually enabled with the following build configuration:

```sbt
enableAkkaClusterBootstrap := true
```

When you generate resources for a deployment, you'll need to specify a replica count greater than 2. A value of 3 or
5 is recommended. This is to reduce the risk of multiple islands of clusters being formed when first formed. For example,
the following command specifies 5 replicas which are used as the contact points for forming the cluster:

```bash
rp generate-kubernetes-resources my-org/my-app:0.1.0 --pod-controller-replicas 5
```

By default, the feature will be configured to communicate and join other applications with the same name. If you have two disparate applications
that should join the same cluster, use the `akkaClusterBootstrapSystemName` setting. For example, all applications that specify the following
will join the same cluster, even if they have different names:

```sbt
akkaClusterBootstrapSystemName := "my-actor-system"
```

You can use the `--join-existing-akka-cluster` flag to ensure that a new cluster will never be formed. This can be useful
if you expect the cluster to never be fully down. It's also quite useful for launching one-off processes or jobs that
are intended to join an already bootstrapped cluster.

```bash
rp generate-kubernetes-resources my-org/my-app:0.1.0 --join-existing-akka-cluster
```

When deploying applications that use Akka Cluster, you'll typically want them to join the same cluster. This is especially true if you
use Akka Persistence features. Because of this, it is recommended that you avoid Blue/Green deployments and instead use
the Canary (default) or Rolling deployment types

## Applications & Jobs

You can declare additional *applications* inside your Docker image. An application is a name and a sequence of arguments
that should be executed for the application. An operator can then generate alternative resources using `rp` by simply
declaring the application name.

On Kubernetes, this is complemented by the support for generating `Job` resources. For example, the following configuration would
result in the main method defined by `App` being run by default. However, the operator can invoke `rp generate-kubernetes-resources --application my-job`
to generate resources for the defined application instead.

Given the following build configuration:

```sbt
mainClass in Compile := Some("com.example.App")

applications += "my-job" -> Vector("bin/myjob")

```

And, given the following main classes:

```scala
package com.example

object App {
  def main(args: Array[String]): Unit = { ... }
}

object MyJob {
  def main(args: Array[String]): Unit = { ... }
}
```

An operator can generate resources for `App` by using the `--application` flag. Additionally, the `--pod-controller-type`
flag is provided to allow a `Job` to be generated instead of a `Deployment`.

```bash
# Creates a Deployment that runs App
rp generate-kubernetes-resources my-org/my-app:0.1.0

# Creates a Job that runs MyJob
rp generate-kubernetes-resources my-org/my-app:0.1.0 \
  --application my-job \
  --pod-controller-type job
```

## Docker & JVM Configuration

The Docker image is the deployment unit of choice and powers a wide variety of orchestration systems, including Kubernetes and DC/OS. The docker images created by Lightbend Orchestration offer the following default configuration:

1. Minimal base-image, `openjdk:alpine`, to reduce the size of the images produced.
2. JVM's Docker CPU and memory limits are enabled, as discussed on this Oracle [Blog Post](https://blogs.oracle.com/java-platform-group/java-se-support-for-docker-cpu-and-memory-limits).

Additionally, a convenience setting is provided, `alpinePackages`, which can be used to specify additional Alpine packages that should be installed when building the Docker image. For example, the following build configuration ensures `core-utils` is added to the Docker image:

```sbt
alpinePackages += "coreutils"
```

## Endpoint Detection & Declaration

An endpoint defines how your application communicates with other services and the outside world. Endpoints have a name and optionally an ingress declaration. Ingress defines how your application should be accessible from outside of the cluster, whether that be via path-based routing, virtual hosts, or simply by load balancer ports.

#### Project

Endpoints are specified using the `endpoints` sbt setting. For example, the following declares an endpoint, `http`, that is available on ports `80` and `443` via the virtual host `myservice.example.org`:

```sbt
endpoints += HttpEndpoint(
  name = "http",
  ingress = HttpIngress(
    ingressPorts = Vector(80, 443),
    hosts = Vector("myservice.example.org"),
    paths = Vector.empty))
```

For Play applications, an endpoint, `http`, is automatically added for Play's HTTP server.

For Lagom applications, an endpoint, `http` is automatically declared for each microservice with the appropriate ingress settings. The service locator offers tight integration with it, allowing you to find other services by simply looking up their service name.

#### Kubernetes

When generating configuration for Kubernetes using the `rp` tool, [Service](https://kubernetes.io/docs/concepts/services-networking/service/) declarations are created for each endpoint. Additionally, if any ingress settings are defined for the endpoint, the appropriate [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) resources will be created and configured to point at the appropriate service.

#### DC/OS

When generating configuration for DC/OS using the `rp` tool, [Marathon Port Definitions](https://mesosphere.github.io/marathon/docs/ports.html) are generated for each microservice. Additionally, if any ingress settings are defined for the endpoint, the appropriate [Marathon-lb](https://github.com/mesosphere/marathon-lb) configuration is defined.

## Secrets

A non-blocking secrets API is available for Kubernetes. The secrets must be declared in the `build.sbt` file, and then they can be accessed at runtime using the provided libraries. This feature will be made available for DC/OS at a later date.

#### Project

For example, given the following `build.sbt` setting:

```sbt
secrets += Secret("my-secret", "my-key")
```

A developer can access this setting at runtime:

```scala
import com.lightbend.rp.secrets.scaladsl.SecretReader

val secret: Future[Option[ByteString]] =
  SecretReader.get(name = "my-secret", key = "my-key")
```

#### Kubernetes

An operator can declare this secret before generating the deployment resources:

```bash
kubectl create secret generic my-secret --from-file=my-key=./path-to-my-secret-file
```

## Service Location

Service location facilities are provided by the tooling. This allows you to seamlessly discover other services without having to worry about how that maps to DNS addresses or how to asynchronously perform DNS lookups. To find other services, you simply need to specify the service and endpoint names. If the services are in a different namespace, that can also be specified.

### Configuration

This feature is automatically enabled for Lagom applications <sup>1</sup>.
It can be manually enabled with the following build configuration:

```sbt
enableServiceDiscovery := true
```

<sup>1</sup> Note that while it is enabled, your Lagom application still needs to be modified to bind the service locator. Use
the configuration below to do that.

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

### Usage

Below, you'll find some example code that uses the service locator to find the `http` endpoint of `my-service`:

```scala
import com.lightbend.rp.servicediscovery.scaladsl.ServiceLocator

val service: Future[Option[Service]] =
  ServiceLocator.lookupOne(name = "my-service", endpoint = "http")
```

For Lagom applications, a service locator implementation is provided. It will attempt to find other services with the endpoint name `http` by default (following the conventions noted under *Endpoint Detection & Declaration*).

#### External Services

If you have external services, you can use the `--external-service` argument when generating deployment resources. For example, the following command will specify that the service **cas_native** (Lagom's default Cassandra service name)
should point to an external service address (`_cql._tcp.reactive-sandbox-cassandra.default.svc.cluster.local`):

macOS / Linux
:  ```bash
    rp generate-kubernetes-resources my-service-impl:1.0.0 \
      --external-service cas_native=_cql._tcp.reactive-sandbox-cassandra.default.svc.cluster.local
    ```

Windows
:   ```powershell
    rp.exe generate-kubernetes-resources my-service-impl:1.0.0 \
      --external-service cas_native=_cql._tcp.reactive-sandbox-cassandra.default.svc.cluster.local
    ```

## Status

A status facility is provided by the tooling. When enabled, an additional route is added to the Akka Management HTTP
server and the appropriate health and readiness checks are defined. By default, this route responds to
requests to `/platform-tooling/health` and `/platform-tooling/readiness`.

#### Configuration

This feature is automatically enabled for applications that use Akka Cluster. It can be manually enabled with the following build configuration:

```sbt
enableStatus := true
```

#### Extensions

You can extend this facility by defining your own instances of `com.lightbend.rp.status.HealthCheck` and `com.lightbend.rp.status.ReadinessCheck`.
The configuration below shows how you can define a health check that causes your application to become unhealthy after one hour has passed:

```hocon
# In application.conf -- if adding a readiness check,
# add to the `readiness-checks` key instead.

com.lightbend.platform-tooling.health-checks +=
  "com.mycompany.MyAppHealthCheck"
```

```scala
// In your applications' source code. If defining a readiness check,
// extend `ReadinessCheck` with method `ready` instead

class MyAppHealthCheck extends com.lightbend.rp.status.HealthCheck {
  private val startTime = java.time.Instant.now().toEpochMilli
  private val maxTime = startTime + (1000 * 60L * 60L)

  def healthy(actorSystem: ExtendedActorSystem)
             (implicit ec: ExecutionContext): Future[Boolean] =
    Future.successful(startTime < maxTime)
}
```

By default, a readiness check is provided for Akka Cluster applications that succeeds after the cluster has been formed.
