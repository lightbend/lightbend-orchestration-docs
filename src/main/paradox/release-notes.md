# Release Notes

This page tracks the components of Lightbend Orchestration. Note that `reactive-lib` is automatically included by `sbt-reactive-app`, so you do not normally
need to specify its version. However, a particular version can be used by explicitly setting the `reactiveLibVersion`
sbt setting.

## Latest Versions

@@@vars
| Project                                                                     |  Version |
|-----------------------------------------------------------------------------|----------|
| [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app/releases)  | $sbtreactiveapp$   |
| [reactive-cli](https://github.com/lightbend/reactive-cli/releases)          | $reactivecli$  |
| [reactive-lib](https://github.com/lightbend/reactive-lib/releases)          | $reactivelib$  |
@@@

## sbt-reactive-app 1.7.3

- Fixes Play 2.7 endpoint detection [sbt-reactive-app#184][sbt-reactive-app184]
- Fixes Lagom http port detection [sbt-reactive-app#182][sbt-reactive-app182]
- Fixes rp-application.conf becoming blank when deploying Play without reactive-lib modules [sbt-reactive-app#180][sbt-reactive-app180]
- Makes unused `ingressPorts` optional in `HttpIngress` [sbt-reactive-app#181][sbt-reactive-app181]

  [sbt-reactive-app184]: https://github.com/lightbend/sbt-reactive-app/pull/184
  [sbt-reactive-app182]: https://github.com/lightbend/sbt-reactive-app/pull/182
  [sbt-reactive-app181]: https://github.com/lightbend/sbt-reactive-app/pull/181
  [sbt-reactive-app180]: https://github.com/lightbend/sbt-reactive-app/pull/180

## 1.7.1

### sbt-native-packager 1.3.18

sbt-reactive-app 1.7.1 updates its dependency to sbt-native-packager 1.3.18 to bring in some bug fixes around Docker image building.

### Other fixes

- Fixes `-Dplay.server.pidfile.path=/dev/null` generation. [cli#201][cli201]
- Default `rp` to `--registry-use-local`. `--registry-disable-local` is added to disable it. [cli#203][cli203]

  [cli201]: https://github.com/lightbend/reactive-cli/pull/201
  [cli203]: https://github.com/lightbend/reactive-cli/pull/203

## 1.7.0

In general, the theme of Lightbend Orchestration 1.7.0 is to remove unnecessary features to make the deployment leaner and more manageable. 1.7.0 runtime is based on Akka 2.5.20 and Akka Management 0.20.0.

### Secure Docker image building with sbt-native-packager 1.3.17

For building Docker images, Lightbend Orchestration depends on sbt-native-packager, an sbt plugin maintained by Nepomuk "Muki" Seiler. To improve the security around file permissions and Red Hat OpenShift compatibility, Lightbend Tooling team has contributed a few enhancements to sbt-native-packager.

First, `dockerPermissionStrategy` was added to decide how file permissions are set for the working directory inside the Docker image. The default `DockerPermissionStrategy.MultiStage` strategy uses multi-stage Docker build to call chmod ahead of time. This avoids extra Docker layer overhead.

Next, `dockerChmodType` setting was added to specify what file permissions are set for the working directory. By default, it uses `DockerChmodType.UserGroupReadExecute`, which prevents the working directory to be writable. If you want your application to write a file, the following setting can be used to opt-in:

```scala
import com.typesafe.sbt.packager.docker.DockerChmodType
dockerChmodType := DockerChmodType.UserGroupWriteExecute
```

See [sbt-native-packager 1.3.16](https://github.com/sbt/sbt-native-packager/releases/tag/v1.3.16) release note for more details.

### YAML file generation for Akka Cluster Bootstrapping using Kubernetes API

The main feature of Lightbend Orchestration is the automatic generation of Kubernetes configuration  (YAML) files.

For Akka Cluster Bootstrapping, Lightbend Orchestration generates YAML files using Kubernetes API as the discovery method. Starting with Lightbend Orchestration 1.7.0, we will use a specialized label `akka.lightbend.com/service-name`, which denotes the Akka Cluster to join when a pod comes up.

- The value of the this label will default to either the app name or the app name + version depending on the deployment type.
- Deployment pods are labeled with `"akka.lightbend.com/service-name": "friendimpl"` etc.
- You can override the label selector as follows: `-Dakka.discovery.kubernetes-api.pod-label-selector=akka.lightbend.com/service-name=%s` (as opposed to using `app=%s`).
- You can override the  effective name  as follows: `-Dakka.management.cluster.bootstrap.contact-point-discovery.effective-name=friendimpl` etc.

### YAML file generation: Removal of automatic port assignment

Previous releases of Lightbend Orchestration automatically assigned various port numbers from port 10000 in part by overriding your `application.config` file. Lightbend Orchestration 1.7.0 removes this feature, and respects the port number declared in your your `application.config`. Otherwise, default port numbers will be used such as port 9000 for Play. This also allows us to remove `RP_ENDPOINT_*` environment variables, generally simplifying the generated YAML file.

**Note**: This also means that your deployed service will expose different port number (for example 9000) instead of 10000.

### YAML file generation for Akka Cluster Bootstrapping using DNS

Optionally, Lightbend Orchestration 1.7.0 adds **experimental** support to generate Kubernetes configuration for Akka Cluster Bootstrapping using DNS as the discovery method.

If you want to use DNS, pass `--discovery-method=akka-dns` to the `rp` command line. [cli#195][cli195]

### Rename of sbt-reactive-app key names

All key names are renamed to prefix with `rp` and camel cased to comply with [Plugins Best Practices][best-practice]. For instance, `endpoints` setting will now be `rpEndpoints`, and `deploy` task will be `rpDeploy`. The old key names are deprecated and will be removed in the future. [sbt-reactive-app#145][sbt-reactive-app145]

### Deprecation of SecretReader

In the effort to reduce runtime dependencies, SecretReader was deprecated. Read from the file `/rp/secrets/%name%/%key%` where `%name%` is transformed to lowercase, and `-` for non-alphanum instead. [lib#118][lib118]

### Other bug fix

- Fixes missing `protocol` when UDP endpoint is selected. [cli#196][cli196]

  [cli195]: https://github.com/lightbend/reactive-cli/pull/195
  [cli196]: https://github.com/lightbend/reactive-cli/pull/196
  [lib118]: https://github.com/lightbend/reactive-lib/pull/118
  [lib119]: https://github.com/lightbend/reactive-lib/pull/119
  [sbt-reactive-app145]: https://github.com/lightbend/sbt-reactive-app/pull/145
  [best-practice]: https://www.scala-sbt.org/1.x/docs/Plugins-Best-Practices.html

## sbt-reactive-app 1.6.1

- Fixes binary compatibility issue with sbt-native-packager 1.3.15 adopted by Play 2.6.20+ and Lagom 1.4.10. [#169][sbt-reactive-app169] by [@ignasi35][@ignasi35]

## 1.6.0

The reactive-lib 1.6.0 runtime is based on [Akka 2.5.18][akka2518] and [Akka Management][management] 0.20.0.

### Akka Cluster bootstrap: port-name

To maintain the compatibility with Akka Management 0.20.0, reactive-lib 0.9.3+ sets the default value of `akka.management.cluster.bootstrap.contact-point-discovery.port-name` in the config to `"akka-mgmt-http"`. [lib#90][lib90]

In addition, reactive-cli 1.6.0 will generate YAML that passes `-Dakka.management.cluster.bootstrap.contact-point-discovery.port-name` explicitly from the command line, which allows us to incrementally transition to the port name `"management"` in the future. [cli#185][cli185]

### Akka Cluster bootstrap: discovery-method

Instead of using `-Dakka.discovery.method`, reactive-cli 1.6.0 will generate YAML that passes `-Dakka.management.cluster.bootstrap.contact-point-discovery.discovery-method`, which is a setting more specific to Akka Cluster bootstrap. [cli#184][cli184]

Since reactive-lib's `ClusterServiceDiscovery` was never used, the implementation and `akka.discovery.method = reactive-lib` in config were both removed. [lib#99][lib99]

### Other fixes

- Since Akka 2.5.14+ comes with async-dns, reactive-lib's async-dns implementation was removed. Integration was added to test Akka Cluster bootstrap using DNS on OpenShift. [lib#91][lib91] by [@eed3si9n][@eed3si9n]
- Fixes handling of non-defined `rpDockerPublish`. [sbt-reactive-app#153][sbt-reactive-app153] by [@dwijnand][@dwijnand]
- Updates the reference to Lightbend helm-charts repo. [sbt-reactive-app#159][sbt-reactive-app159] by [@marcoderama][@marcoderama]
- To fix "No configuration setting found for key 'decode-max-size'", reactive-lib 1.6.0 depends on Akka HTTP 10.0.15. [lib#105][lib105] by [@eed3si9n][@eed3si9n]
- Fixes a typo in Helm RBAC setup. [sbt-reactive-app#162][sbt-reactive-app162] by [@yoks][@yoks]
- Fixes inclusion of `application.conf` from transitive dependencies. [sbt-reactive-app#165][sbt-reactive-app165] by [@cunei][@cunei] and [@eed3si9n][@eed3si9n]

  [orchestration]: https://developer.lightbend.com/docs/lightbend-orchestration/current/
  [management]: https://developer.lightbend.com/docs/akka-management/current/
  [akka2518]: https://akka.io/blog/news/2018/10/07/akka-2.5.18-released
  [cli]: https://developer.lightbend.com/docs/lightbend-orchestration/current/setup/cli-installation.html
  [sbt-reactive-app]: https://developer.lightbend.com/docs/lightbend-orchestration/current/setup/project-setup.html
  [lib90]: https://github.com/lightbend/reactive-lib/pull/90
  [lib91]: https://github.com/lightbend/reactive-lib/pull/91
  [lib99]: https://github.com/lightbend/reactive-lib/pull/99
  [lib105]: https://github.com/lightbend/reactive-lib/pull/105
  [cli184]: https://github.com/lightbend/reactive-cli/pull/184
  [cli185]: https://github.com/lightbend/reactive-cli/pull/185
  [sbt-reactive-app153]: https://github.com/lightbend/sbt-reactive-app/pull/153
  [sbt-reactive-app159]: https://github.com/lightbend/sbt-reactive-app/pull/159
  [sbt-reactive-app162]: https://github.com/lightbend/sbt-reactive-app/pull/162
  [sbt-reactive-app165]: https://github.com/lightbend/sbt-reactive-app/pull/165
  [sbt-reactive-app169]: https://github.com/lightbend/sbt-reactive-app/pull/169
  [@cunei]: https://github.com/cunei
  [@eed3si9n]: http://github.com/eed3si9n
  [@dwijnand]: https://github.com/dwijnand
  [@marcoderama]: https://github.com/marcoderama
  [@yoks]: https://github.com/yoks
  [@ignasi35]: http://github.com/ignasi35
