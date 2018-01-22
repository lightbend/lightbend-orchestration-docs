# Kubernetes Deployment

Once you've installed the deployment tool, `reactive-cli`, you can use the `rp` program it provides to generate Kubernetes resources. These will then need to be supplied to `kubectl` to complete the deployment.

### Prerequisites

* [reactive-cli](https://github.com/lightbend/reactive-cli) installed
* Applications built and published to a registry (production) or the local docker engine ([Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/))
* `kubectl` installed and configured to point to your registry

> Using Minikube? Make sure you run `eval $(minikube docker-env)` before following the steps below. When you need your old environment, you can get it back with `eval $(minikube docker-env -u)`. 

## Deployments

Now that you've built and published your project, you can use the `rp` program to generate resources for your Kubernetes cluster. Below, we'll cover the deployment of a service from [Chirper](https://github.com/longshorej/lagom-java-chirper-tooling-example), a Lagom-based Twitter Clone. Be sure to refer to [our examples](examples.md) for more thorough documentation.

### Deployment Overview

The command below will deploy version `1.0.0` of `activator-lagom-java-chirper/front-end` to your Kubernetes cluster. Note that you can specify arbitrary environment variables using the `--env` flag; this is handy when having to specifiy, for instance, the application secret for Play applications. Be sure to consult `rp --help` to see a complete listing of available options and flags.

macOS / Linux
:   ```bash
rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

Windows
:   ```powershell
rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

> You can also save the output to a directory using the `-o` argument, or to a file via pipe redirection (i.e. `> my-resources.yml`). This allows you to make any manual enhancements.

### Partial Deployments & Upgrades

`rp` allows you to generate a subset of resources when provided with the proper flags; `--generate-pod-controllers`, `--generate-services`, and `--generate-ingress`. When specified, only those resource types specified will be generated. You can mix and match these arguments, for instance, specifying `--generate-services` and `--generate-ingress` will generate `Ingress` and `Service` resources. When no generation flags are present (default), all resource types are generated.

By combing this with `--deployment-type`, you can easily use these features to perform canary deployments, blue-green deployments, and rolling upgrades.

#### Canary Deployments

[Canary](https://martinfowler.com/bliki/CanaryRelease.html) is the default mode of deployment for `rp`. Under this mode, the new version
will exist alongside the old one and requests will be load-balanced between the old instances and the new ones. You can
adjust the ratio of traffic with the `--pod-deployment-replicas` option.

> By default, instances of a given application that use Akka Cluster will all join the same cluster (indicated by application name) under this mode.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0 with 10 replicas
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0 with 1 replica, thus ensuring a 10-1 ratio of traffic
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 1 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Move 2.1.0 to 10 replicas (1-1 ratio)
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Remove 2.0.0's Pod Controller
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
    --generate-pod-controllers | kubectl delete -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0 with 10 replicas
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0 with 1 replica, thus ensuring a 10-1 ratio of traffic
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 1 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Move 2.1.0 to 10 replicas (1-1 ratio)
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
    --pod-deployment-replicas 10 \
    --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Remove 2.0.0's Pod Controller
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
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
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0" \
      --deployment-type blue-green \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0's Pod Controller (note --generate-pod-controllers)
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0" \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Verify both versions are running
    #
    kubectl get all --namespace activator-lagom-chirper
    #
    # Point traffic to 2.1.0 (note --generate-ingress --generate-services)
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0" \
      --deployment-type blue-green --generate-ingress --generate-services \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Once you're ready, remove 2.0.0's Pod Controller (note --generate-pod-controllers)
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0" \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl delete -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0" \
      --deployment-type blue-green \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Install 2.1.0's Pod Controller (note --generate-pod-controllers)
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0" \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Verify both versions are running
    #
    kubectl get all
    #
    # Point traffic to 2.1.0 (note --generate-ingress --generate-services)
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0" \
      --deployment-type blue-green --generate-ingress --generate-services \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Once you're ready, remove 2.0.0's Pod Controller (note --generate-pod-controllers)
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0" \
      --deployment-type blue-green --generate-pod-controllers \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl delete -f -
    ```

#### Rolling Upgrades

To use [rolling upgrades](https://kubernetes.io/docs/tutorials/kubernetes-basics/update-intro/) with `rp`, provide
the `--deployment-type rolling` option. You'll need to do this when you first install your application and when you
upgrade it. With Rolling Deployments, `Service` and `Pod Controller` resources are simply named after the application.
When you perform a second `rp generate-kubernetes-deployment`, `kubectl` will simply update the image and Kubernetes
will perform the rolling upgrade for you. If you added any new endpoints to your application, they'll be created
as `Service` and `Ingress` (if applicable) resources as well.

> By default, instances of a given application that use Akka Cluster will all join the same cluster (indicated by application name) under this deployment type.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Upgrade to 2.1.0
    #
    rp generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.0.0"  \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    #
    # Upgrade to 2.1.0
    #
    rp.exe generate-kubernetes-deployment "activator-lagom-java-chirper/front-end:2.1.0"  \
      --pod-deployment-replicas 3 --deployment-type rolling \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | kubectl apply -f -
    ```


## Additional Settings

### jq

> You must have `jq` installed and available on the path for this feature to work. You can use `rp version` to verify if `jq` support is available.

`rp` is intentionally limited in scope for the types of resources and different fields it generates. If you wish to arbitrarily modify its output, you can leverage its [jq](https://stedolan.github.io/jq/) support. This allows you to dynamically transform the output and add or change fields. For instance, the following command adds another label to the  `Service` resource:

macOS / Linux
:   ```bash
    rp generate-kubernetes-deployment lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --generate-services \
      --transform-services '.metadata.labels.appCollection = "chirper"'
    ```

Windows
:   ```powershell
    rp.exe generate-kubernetes-deployment lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --generate-services \
      --transform-services '.metadata.labels.appCollection = "chirper"'
    ```

### Namespaces

`rp` contains a namespace flag, `--namespace` that will ensure that resources will be generated as part of the provided namespace. Be sure to consult the Kubernetes [documentation on Namespaces](https://kubernetes.io/docs/concepts/overview/working-with-objects/namespaces/) for more information on namespaces.

The following command will create the resources for the hello-world application, but ensure they're part of the `hello` namespace:

macOS / Linux
:   ```bash
    rp generate-kubernetes-deployment hello/world:1.0.0 --namespace hello | kubectl apply -f -
    ```

Windows
:   ```powershell
    rp.exe generate-kubernetes-deployment hello/world:1.0.0 --namespace hello | kubectl apply -f -
    ```

To see the resources that were generated, use the following command:

```bash
kubectl get all --namespace hello
```

### Private Docker Registry

If your image is stored in a private registry, you'll need to create a credentials file in `~/.lightbend/docker.credentials` so that `rp` can access your image's manifest. For example, the file below configures credentials for the `my-docker-registry.bintray.io` registry.

```
registry = my-docker-registry.bintray.io
username = my-username
password = my-password
```

Lastly, you'll need to ensure that your Kubernetes cluster has access to your registry. (todo: Help reader find the information on how to do this.) 