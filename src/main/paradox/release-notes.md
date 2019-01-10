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
