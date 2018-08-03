## Endpoint Detection and Declaration

An endpoint defines how your application communicates with other services and the outside world. Endpoints have a name and optionally an ingress declaration. Ingress defines how your application should be accessible from outside of the cluster, whether that be via path-based routing, virtual hosts, or simply by load balancer ports.

### Project

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

### Kubernetes

When generating configuration for Kubernetes using the `rp` tool, [Service](https://kubernetes.io/docs/concepts/services-networking/service/) declarations are created for each endpoint. Additionally, if any ingress settings are defined for the endpoint, the appropriate [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) resources will be created and configured to point at the appropriate service.

@@@ note
Integration with Kubernetes except for Minikube is **Incubating** at this point.
@@@

### DC/OS

When generating configuration for DC/OS using the `rp` tool, [Marathon Port Definitions](https://mesosphere.github.io/marathon/docs/ports.html) are generated for each microservice. Additionally, if any ingress settings are defined for the endpoint, the appropriate [Marathon-lb](https://github.com/mesosphere/marathon-lb) configuration is defined.

<link rel="stylesheet" type="text/css" href="../css/custom.css">

@@@ note
Integration with DC/OS is **Incubating** at this point.
@@@
