# Kubernetes Deployment

<link rel="stylesheet" type="text/css" href="css/custom.css">

@@@ note
Integration with Kubernetes except for Minikube is **Incubating** at this point.
@@@

Once you've installed the deployment tool, `reactive-cli`, you can use the `rp` program it provides to generate Kubernetes resources. These will then need to be supplied to `kubectl` to complete the deployment.

### Prerequisites

* [reactive-cli](https://github.com/lightbend/reactive-cli) installed
* Applications built and published to a registry (production) or the local docker engine ([Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/))
* `kubectl` installed and configured to point to your registry

> Using Minikube? Make sure you run `eval $(minikube docker-env)` before following the steps below. When you need your old environment, you can get it back with `eval $(minikube docker-env -u)`.

## Deployments

Now that you've built and published your project, you can use the `rp` program to generate resources for your Kubernetes cluster. Below, we'll cover the deployment of a service from [Chirper](https://github.com/lagom/lagom-java-sbt-chirper-example), a Lagom-based Twitter Clone. Be sure to refer to @ref[our examples](examples.md) for more thorough documentation.

### Deployment Overview

The command below will deploy version `1.0.0` of `activator-lagom-java-chirper/front-end` to your Kubernetes cluster. Note that you can specify arbitrary environment variables using the `--env` flag; this is handy when having to specifiy, for instance, the application secret for Play applications. Be sure to consult `rp --help` to see a complete listing of available options and flags.

macOS / Linux
:   ```bash
rp generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

Windows
:   ```powershell
rp.exe generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

> You can also save the output to a directory using the `-o` argument, or to a file via pipe redirection (i.e. `> my-resources.yml`). This allows you to make any manual enhancements.

### Partial Deployments & Upgrades

`rp` allows you to generate a subset of resources when provided with the proper flags; `--generate-pod-controllers`, `--generate-services`, and `--generate-ingress`. When specified, only those resource types specified will be generated. You can mix and match these arguments, for instance, specifying `--generate-services` and `--generate-ingress` will generate `Ingress` and `Service` resources. To generate all resource types, use `--generate-all`.

By combing this with `--deployment-type`, you can easily use these features to perform canary deployments, blue-green deployments, and rolling upgrades.

#### Canary Deployments

[Canary](https://martinfowler.com/bliki/CanaryRelease.html) is the default mode of deployment for `rp`. Under this mode, the new version
will exist alongside the old one and requests will be load-balanced between the old instances and the new ones. You can
adjust the ratio of traffic with the `--pod-deployment-replicas` option.

> By default, instances of a given application that use Akka Cluster will all join the same cluster (indicated by application name)
under this mode. If you use Akka Cluster, be sure to provide the `--akka-cluster-join-existing` flag if your services
have already been  deployed. This will guarantee the new pods will only join an already formed cluster.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0 with 10 replicas
    #
    rp generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.0.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0 with 1 replica, thus ensuring a 10-1 ratio of traffic
    #
    rp generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 1 --akka-cluster-join-existing \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Move 2.1.0 to 10 replicas (1-1 ratio)
    #
    rp generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 10 --akka-cluster-join-existing \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Remove 2.0.0's Pod Controller
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0"  \
    --generate-pod-controllers | kubectl delete -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0 with 10 replicas
    #
    rp.exe generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.0.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0 with 1 replica, thus ensuring a 10-1 ratio of traffic
    #
    rp.exe generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 1 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Move 2.1.0 to 10 replicas (1-1 ratio)
    #
    rp.exe generate-kubernetes-resources --generate-all "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Remove 2.0.0's Pod Controller
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0"  \
    --generate-pod-controllers | kubectl delete -f -
    ```

#### Blue/Green Deployments

You can also use `rp` to help facilitate [blue/green](https://docs.cloudfoundry.org/devguide/deploy-apps/blue-green.html)
deployments. This is a multi-step process and for each execution of `rp`, you must provide the `--deployment-type blue-green`
option. First, you will install the new version's Pod Controllers. Then, once satisfied, you'll simply update
the `Service` and `Ingress` resources. You can then remove the old Pod Controllers. See below for an example:

> Blue/Green deployments, when combined with Akka Cluster, will by default result in two separate Akka Clusters
being formed. This is not usually what you want, in particular if you are using any sort of persistence features.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0" \
      --generate-all \
      --deployment-type blue-green \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0's Pod Controller (note --generate-pod-controllers)
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0" \
      --generate-all \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Verify both versions are running
    #
    kubectl get all --namespace activator-lagom-chirper
    #
    # Point traffic to 2.1.0 (note --generate-ingress --generate-services)
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0" \
      --generate-all \
      --deployment-type blue-green --generate-ingress --generate-services \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Once you're ready, remove 2.0.0's Pod Controller (note --generate-pod-controllers)
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0" \
      --generate-all \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl delete -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0" \
      --generate-all \
      --deployment-type blue-green \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0's Pod Controller (note --generate-pod-controllers)
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0" \
      --generate-all \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Verify both versions are running
    #
    kubectl get all
    #
    # Point traffic to 2.1.0 (note --generate-ingress --generate-services)
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0" \
      --generate-all \
      --deployment-type blue-green --generate-ingress --generate-services \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Once you're ready, remove 2.0.0's Pod Controller (note --generate-pod-controllers)
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0" \
      --generate-all \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl delete -f -
    ```

#### Rolling Upgrades

To use [rolling upgrades](https://kubernetes.io/docs/tutorials/kubernetes-basics/update-intro/) with `rp`, provide
the `--deployment-type rolling` option. You'll need to do this when you first install your application and when you
upgrade it. With Rolling Deployments, `Service` and `Pod Controller` resources are simply named after the application.
When you perform a second `rp generate-kubernetes-resources`, `kubectl` will simply update the image and Kubernetes
will perform the rolling upgrade for you. If you added any new endpoints to your application, they'll be created
as `Service` and `Ingress` (if applicable) resources as well.

> By default, instances of a given application that use Akka Cluster will all join the same cluster (indicated by application name) under this deployment type. If
you use Akka Cluster, be sure to provide the `--akka-cluster-join-existing` flag if your services have already been deployed. This will guarantee the new pods
will only join an already formed cluster.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0"  \
      --generate-all \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Upgrade to 2.1.0
    #
    rp generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0"  \
      --generate-all \
      --pod-deployment-replicas 3 --deployment-type rolling --akka-cluster-join-existing \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.0.0"  \
      --generate-all \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Upgrade to 2.1.0
    #
    rp.exe generate-kubernetes-resources "activator-lagom-java-chirper/front-end:2.1.0"  \
      --generate-all \
      --pod-deployment-replicas 3 --deployment-type rolling --akka-cluster-join-existing \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

## Multiple Images

`rp generate-kubernetes-resources` can also be used to generate the resources for more than one image. This is useful in particular for `Ingress` resource generation, where many controllers do not allow multiple `Ingress` definitions for the same host. Refer to the @ref[Examples](examples.md) page to see the syntax for this usage. [Chirper](https://github.com/lagom/lagom-java-sbt-chirper-example) in particular makes use of this feature for generating its `Ingress` resource.

## Best Practices

`rp` attempts to be flexible and integrate well into your existing workflow. It's recommended that you create and
version control a script for invoking `rp` with its various options so that you can audit your changes.

Some users may choose to take this a step further and version control the output of the tool as well.

## Additional Settings

### jq

> You must have `jq` installed and available on the path for this feature to work. You can use `rp version` to verify if `jq` support is available.

`rp` is intentionally limited in scope for the types of resources and different fields it generates. If you wish to arbitrarily modify its output, you can leverage its [jq](https://stedolan.github.io/jq/) support. This allows you to dynamically transform the output and add or change fields. For instance, the following command adds another label to the  `Service` resource:

macOS / Linux
:   ```bash
    rp generate-kubernetes-resources lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --generate-all \
      --generate-services \
      --transform-services '.metadata.labels.appCollection = "chirper"'
    ```

Windows
:   ```powershell
    rp.exe generate-kubernetes-resources lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --generate-all \
      --generate-services \
      --transform-services '.metadata.labels.appCollection = "chirper"'
    ```

### Namespaces

`rp` contains a namespace flag, `--namespace` that will ensure that resources will be generated as part of the provided namespace. Be sure to consult the Kubernetes [documentation on Namespaces](https://kubernetes.io/docs/concepts/overview/working-with-objects/namespaces/) for more information on namespaces.

The following command will create the resources for the hello-world application, but ensure they're part of the `hello` namespace:

macOS / Linux
:   ```bash
    rp generate-kubernetes-resources hello/world:1.0.0 --generate-all --generate-namespaces --namespace hello | kubectl apply -f -
    ```

Windows
:   ```powershell
    rp.exe generate-kubernetes-resources hello/world:1.0.0 --generate-all --generate-namespaces --namespace hello | kubectl apply -f -
    ```

To see the resources that were generated, use the following command:

```bash
kubectl get all --namespace hello
```

### Private Docker Registry

Docker images you build using the sbt-reactive-app plugin will need to be accessed by the CLI. Depending on where you put them, the CLI might need your authentication credentials to be able to access the registry. It will try to read credentials stored locally on your system by docker after you authenticate:

```bash
docker login my-docker-registry.bintray.io
```
When reading these credentials the CLI might prompt to enter your user password, since the data is stored in a secure OS-specific enclave. If you don't want this, it is possible to explicitly provide your credentials by writing them down to `~/.lightbend/docker.credentials` (Linux, macOS) or `%HOMEPATH%\.lightbend\docker.credentials` (Windows) file:

```
registry = my-docker-registry.bintray.io
username = foo
password = bar

registry = my-docker-registry2.bintray.io
username = foo2
password = bar2
```
