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

<link rel="stylesheet" type="text/css" href="../css/custom.css">

@@@ warning
Integration with all deployment targets except for Minikube is **Incubating** at this point.
@@@
