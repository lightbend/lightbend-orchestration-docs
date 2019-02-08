## Secrets

A non-blocking secrets API is available for Kubernetes. The secrets must be declared in the `build.sbt` file, and then they can be accessed at runtime using the provided libraries.
#### Project

For example, given the following `build.sbt` setting:

```sbt
secrets += Secret("my-secret", "my-key")
```

A developer can access this setting at runtime by reading from the file `/rp/secrets/%name%/%key%` where `%name%`
is transformed to lowercase, and `-` for non-alphanum instead.

#### Kubernetes

<link rel="stylesheet" type="text/css" href="../css/custom.css">

@@@ note
Integration with Kubernetes except for Minikube is **Incubating** at this point.
@@@

An operator can declare this secret before generating the deployment resources:

```bash
kubectl create secret generic my-secret --from-file=my-key=./path-to-my-secret-file
```
