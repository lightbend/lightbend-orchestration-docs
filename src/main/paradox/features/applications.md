## Additional Applications and Jobs

@@include[deprecation.md](deprecation.md)

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
