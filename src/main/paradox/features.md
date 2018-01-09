# Features Reference

## Akka Cluster Formation

When enabled, this feature will use service discovery to identify other contact points. Once the requisite number of contact points have been identified, the one with the lowest address will form the cluster. The other nodes will then join that one.

This feature is automatically enabled for Lagom applications that require it. It can be manually enabled with the following build configuration:

```sbt
enableAkkaClusterBootstrap := Some(true)

enableServiceDiscovery := true
```

Additionally, when you generate resources for deployment, you'll need to specify a replica count greater than 2. A value of 3 or 5 is recommended. This is to reduce the risk of multiple islands of clusters being formed when first formed. For example, the following command specifies 5 replicas which are used as the contact points for forming the cluster:

```bash
rp generate-kubernetes-deployment my-org/my=app:0.1.0 --pod-controller-replicas 5
```
 
## Docker & JVM Configuration

The Docker image is the deployment unit of choice and powers a wide variety of orchestration systems, including Kubernetes. The docker images created by the Platform Tooling offer the following default configuration:

1) Minimal base-image, `openjdk:alpine`, to reduce the size of the images produced.
2) Docker repository is set based on the project name. For example, given a Lagom root project `my-system` and a service `my-svc-impl`, the image's name will default to `my-system/my-svc-impl`.
3) JVM's Docker CPU and memory limits are enabled, as discussed on this Oracle [Blog Post](https://blogs.oracle.com/java-platform-group/java-se-support-for-docker-cpu-and-memory-limits).

## Endpoint Detection & Declaration

An endpoint defines how your application communicates with other services and the outside world. Endpoints have a name and optionally an ingress declaration. Ingress defines how your application should be accessible from outside of the cluster, whether that be via path-based routing, virtual hosts, or simply by load balancer ports.

#### Project

Endpoints are specified using the `endpoints` SBT setting. For example, the following declares an endpoint, `http`, that is available on ports `80` and `443` via the virtual host `myservice.example.org`:

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

When generating deployments for Kubernetes using the `rp` tool, [Service](https://kubernetes.io/docs/concepts/services-networking/service/) declarations are created for each endpoint. Additionally, if any ingress settings are defined for the endpoint, the appropriate [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) resources will be created and configured to point at the appropriate service.


## Secrets

A non-blocking secrets API is available. The secrets must be declared in the `build.sbt` file, and then they can be accessed at runtime using the provided libraries.

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

This feature is automatically enabled for Lagom applications. It can be manually enabled with the following build configuration:

```sbt
enableServiceDiscovery := true
```

Below, you'll find some example code that uses the service locator to find the `http` endpoint of `my-service`:

```scala
import com.lightbend.rp.servicediscovery.scaladsl.ServiceLocator

val service: Future[Option[Service]] = 
  ServiceLocator.lookupOne(name = "my-service", endpoint = "http")
```

Additionally, for Lagom applications, a service locator implementation is provided. It will attempt to find other services with the endpoint name `http` by default (following the conventions noted under *Endpoint Detection & Declaration*).
