# DC/OS Deployment

<link rel="stylesheet" type="text/css" href="css/custom.css">

@@include[deprecation.md](deprecation.md)

Once you've installed the deployment tool, `reactive-cli`, you can use the `rp` program it provides to generate DC/OS (Marathon) resources. These will then need to be supplied to `dcos` to complete the deployment.

### Prerequisites

* [reactive-cli](https://github.com/lightbend/reactive-cli) installed
* Applications built and published to a registry
* `dcos` installed and configured to point to your cluster

## Deployments

Now that you've built and published your project, you can use the `rp` program to generate Marathon configuration for your DC/OS cluster. Below, we'll cover the deployment of a service from [Chirper](https://github.com/lagom/lagom-java-sbt-chirper-example), a Lagom-based Twitter Clone. Be sure to refer to @ref[our examples](examples.md) for more thorough documentation.

### Initial Deployment

The command below will deploy version `1.0.0` of `activator-lagom-java-chirper/front-end` to your DC/OS cluster. Note that you can specify arbitrary environment variables using the `--env` flag; this is handy when having to specifiy, for instance, the application secret for Play applications. Be sure to consult `rp --help` to see a complete listing of available options and flags.

macOS / Linux
:   ```bash
rp generate-marathon-configuration "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | dcos marathon app add
    ```

Windows
:   ```powershell
rp.exe generate-marathon-configuration "activator-lagom-java-chirper/front-end:1.0.0" \
  --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | dcos marathon app add
    ```

> You can also save the output to a file using the `-o` argument, or via pipe redirection (i.e. `> my-config.json`). This allows you to make any manual enhancements.

### Upgrades

When deploying an upgrade to an application, you'll need to run some slightly different commands. The workflow indicated below walks you through this process.

> By default, instances of a given application that use Akka Cluster will all join the same cluster (indicated by application name). If you use Akka Cluster, be sure to provide the `--akka-cluster-join-existing` flag if your services have already been deployed. This will guarantee the new pods will only join an already formed cluster.

macOS / Linux
:   ```bash
    #
    # Initial install of 2.0.0
    #
    rp generate-marathon-configuration front-end:2.0.0  \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | dcos marathon app add
    #
    # Upgrade to 2.1.0
    #
    rp generate-marathon-configuration front-end:2.1.0  \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" \
      --akka-cluster-join-existing | dcos marathon app update /front-end
    ```

Windows
:   ```powershell
    #
    # Initial install of 2.0.0
    #
    rp.exe generate-marathon-configuration front-end:2.0.0  \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" | dcos marathon app add
    #
    # Upgrade to 2.1.0
    #
    rp.exe generate-marathon-configuration front-end:2.1.0  \
      --env JAVA_OPTS="-Dplay.crypto.secret=youmustchangeme4" \
      --akka-cluster-join-existing | dcos marathon app update /front-end
    ```

## Best Practices

`rp` attempts to be flexible and integrate into your existing workflow. It's recommended that you create and version control a script for invoking `rp` with its various options so that you can audit your changes.

Some users may choose to take this a step further and version control the output of the tool as well.

## Additional Settings

### jq

> You must have `jq` installed and available on the path for this feature to work. You can use `rp version` to verify if `jq` support is available.

`rp` is intentionally limited in scope for the types of configuration and different fields it generates. If you wish to arbitrarily modify its output, you can leverage its [jq](https://stedolan.github.io/jq/) support. This allows you to dynamically transform the output and add or change fields. For instance, the following command adds another label to the first application:

macOS / Linux
:   ```bash
    rp generate-marathon-configuration lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --transform-output '.apps[0].labels.appCollection = "chirper"'
    ```

Windows
:   ```powershell
    rp.exe generate-marathon-configuration lagom-java-chirper-tooling-example/front-end:1.0.0-SNAPSHOT \
      --transform-output '.apps[0].labels.appCollection = "chirper"'
    ```

### Namespaces

`rp` contains a namespace flag, `--namespace` that will ensure that resources will be generated as part of the provided namespace. By specifying a namespace, your application configuration will be placed inside an [Application Group](https://mesosphere.github.io/marathon/docs/application-groups.html) with the specified name, and the service locator will be configured to query for services within that application group.

The following command will create the resources for the hello-world application, but ensure they're part of the `hello` application group:

macOS / Linux
:   ```bash
    rp generate-marathon-configuration hello/world:1.0.0 --namespace hello | dcos marathon app add
    ```

Windows
:   ```powershell
    rp.exe generate-marathon-configuration hello/world:1.0.0 --namespace hello | dcos marathon app add
    ```

### Private Docker Registry

Docker images you build using the sbt-reactive-app plugin will need to be accessed by the CLI. Depending on where you put them, the CLI may need your authentication credentials to be able to access the registry. It will try to read credentials stored locally on your system by docker after you authenticate:

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
