# Release Notes

This page tracks the components of Reactive Platform Tooling. When using these projects, you should make sure they all have
the same major version. Note that `reactive-lib` is automatically included by `sbt-reactive-app`, so you do not normally
need to specify its version. However, a particular version can be used by explicitly setting the `reactiveLibVersion`
SBT setting.

## Latest Versions

| Project                                                                     |  Version |
|-----------------------------------------------------------------------------|----------|
| [reactive-cli](https://github.com/lightbend/reactive-cli/releases)          | `0.9.0`  |
| [reactive-lib](https://github.com/lightbend/reactive-lib/releases)          | `0.5.0`  |
| [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app/releases)  | `0.6.0`  |

## reactive-cli

### 0.9.0 - Monday, February 12, 2018

* CLI now requires one (or more) of the following to be specified when generating Kubernetes resources: `--generate-all`, `--generate-services`, `--generate-pod-controllers`, `--generate-ingress`
* Added `--ingress-tls-secret` flag to specify TLS secret for `Ingress` resources.
* Added `pod-controller-restart-policy` flag to modify the restart policy, particular for `Job` resources. 
* Fixed a bug causing incorrect Akka Management Contact Point setting to be specified.
* Fixed a bug causing incompatibility with some private Docker registries.
* Added `--service-load-balancer-ip` flag to set the `loadBalancerIP` for `Service` resources.
* Added `--service-type` flag to set the `type` for `Service` resources.
* Added feature to integrate with existing Docker authentication mechanisms, i.e. `docker login`.
* Added feature to allow resources to be generated from multiple images in one invocation. `Ingress` resources in particular are now merged by host if more than one is generated.

## reactive-lib

### 0.5.0 - Monday, February 12, 2018

* Lagom Java Service Locator is no longer enabled by default.
* Visibility of healthy/readiness methods changed to public for better integration into user's applications

## sbt-reactive-app

### 0.6.1 - Tuesday, February 13, 2018

* Fixed a bug causing the plugin to fail on Lagom projects using SBT 1.0 or later.

### 0.6.0 - Monday, February 12, 2018

* Upgraded automatic inclusion of reactive-lib to `0.5.0`.
* Remove automatic setting of `dockerUsername`.
* Add a feature, `sbt 'deploy minikube'` that automatically deploys all projects into local Minikube
