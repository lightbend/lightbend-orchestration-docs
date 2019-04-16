## Service Location

@@include[deprecation.md](deprecation.md)

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
