## Application Health

@@include[deprecation.md](deprecation.md)

A status facility is provided by the tooling. When enabled, an additional route is added to the Akka Management HTTP
server and the appropriate health and readiness checks are defined. By default, this route responds to
requests to `/platform-tooling/healthy` and `/platform-tooling/ready`.

### Configuration

This feature is automatically enabled for applications that use Akka Cluster. It can be manually enabled with the following build configuration:

```sbt
enableStatus := true
```

### Extensions

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
