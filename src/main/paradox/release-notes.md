# Release Notes

This page tracks the components of Lightbend Orchestration for Kubernetes. Note that `reactive-lib` is automatically included by `sbt-reactive-app`, so you do not normally
need to specify its version. However, a particular version can be used by explicitly setting the `reactiveLibVersion`
SBT setting.

## Latest Versions

| Project                                                                     |  Version |
|-----------------------------------------------------------------------------|----------|
| [reactive-cli](https://github.com/lightbend/reactive-cli/releases)          | `1.0.0`  |
| [reactive-lib](https://github.com/lightbend/reactive-lib/releases)          | `0.7.0`  |
| [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app/releases)  | `1.1.0`  |

## reactive-cli

### 1.0.0 - Tuesday, March 27, 2018

* Fix formatting of Kubernetes default API versions
* Fix error messages for when multiple docker images are not found
* Fix a bug causing `docker login` credentials to be ignored in certain circumstances
* Now supports specifying Docker digest syntax (i.e. `my-project-impl@<somehash>`)
* `RP_NAMESPACE` now sourced via Kubernetes ref spec
* Fix bug with YAML renderer and strings "true" "false" "null"
* Instances that specify `--akka-cluster-join-existing` will no longer be discoverable for bootstrap

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

### 0.7.0 - Tuesday, March 27, 2018

* Add retry mechanism to service locator
* Increase default timeouts for service locator
* Fix configuration of akka management, allowing cluster bootstrap to work with NAT/bridged networking, in particular on DC/OS

### 0.6.0 - Wednesday, March 21, 2018

* Add support for Mesos to service locator
* Fix a service locator bug causing min ttl to be equal to max ttl

### 0.5.0 - Monday, February 12, 2018

* Lagom Java Service Locator is no longer enabled by default.
* Visibility of healthy/readiness methods changed to public for better integration into user's applications

## sbt-reactive-app

### 1.1.0 - Tuesday, March 27, 2017

* Upgraded automatic inclusion of reactive-lib to `0.7.0`.
* Internal changes to support DC/OS

### 1.0.0 - Monday, March 26, 2018

* Upgraded automatic inclusion of reactive-lib to `0.6.0`.
* 'deploy minikube' now validates that ingress addon has been enabled
* One `LABEL` directive is now used, speeding up build times
* Internal changes to support dynamic environments
* `LABEL` order is now preserved

### 0.6.1 - Tuesday, February 13, 2018

* Fixed a bug causing the plugin to fail on Lagom projects using SBT 1.0 or later.

### 0.6.0 - Monday, February 12, 2018

* Upgraded automatic inclusion of reactive-lib to `0.5.0`.
* Remove automatic setting of `dockerUsername`.
* Add a feature, `sbt 'deploy minikube'` that automatically deploys all projects into local Minikube
