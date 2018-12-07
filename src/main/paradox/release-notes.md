# Release Notes

This page tracks the components of Lightbend Orchestration. Note that `reactive-lib` is automatically included by `sbt-reactive-app`, so you do not normally
need to specify its version. However, a particular version can be used by explicitly setting the `reactiveLibVersion`
sbt setting.

## Latest Versions

@@@vars
| Project                                                                     |  Version |
|-----------------------------------------------------------------------------|----------|
| [reactive-cli](https://github.com/lightbend/reactive-cli/releases)          | $reactivecli$  |
| [reactive-lib](https://github.com/lightbend/reactive-lib/releases)          | $reactivelib$  |
| [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app/releases)  | $sbtreactiveapp$   |
@@@

## reactive-cli

### 1.3.2 - Friday, December 7, 2018

* Improvements in the authentication with docker registries, in particular with those
  that only support basic authentication.

### 1.3.0 - Monday, July 16, 2018

* Support `annotations` label in docker images created by sbt-reactive-app. Kubernetes pods will
  have annotations added, DC/OS applications will have labels added.

### 1.2.0 - Thursday, May 24, 2018

* Add a flag `--registry-use-local` which reads local docker images first before trying to reach remote registry

### 1.1.4 - Monday, May 14, 2018

* Fix a bug in Marathon configuration generation

### 1.1.3 - Friday, May 11, 2018

* Fix a bug in HTTP header parsing

### 1.1.2 - Tuesday, May 8, 2018

* Fix a bug in HTTP header parsing
* Add a flag `--stacktrace` to print out the full stack trace when `rp` fails

### 1.1.1 - Monday, April 30, 2018

* Fix a bug with using oauth2 tokens as credentials for docker registry
* Avoid CLI and build plugin version mismatch by requiring sbt-reactive-app 1.1.0 or newer

### 1.1.0 - Sunday, April 15, 2018

* Fix a bug in Kubernetes CPU and memory limit declarations
* Improve Docker credentials, adding support for gcloud helper
* Add feature to generate configuration for Marathon, thus providing DC/OS support.

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

### 0.9.2 - Monday, September 17, 2018

* Update akka-management version to `0.18.0`

### 0.9.1 - Friday, September 14, 2018

* Fixe health check to report healthy during bootstrap
* Make DNS parsing more robust
* Fix ServiceLocator for host name lookup

### 0.9.0 - Wednesday, July 18, 2018

* Update akka-management version to `0.17.0`

### 0.8.2 - Tuesday, July 3, 2018

* Support alternate Kubernetes cluster DNS suffixes from environment
* Update akka-management version to `0.15.0`

### 0.8.1 - Wednesday, May 30, 2018

* Update akka-management version to `0.13.1`

### 0.8.0 - Wednesday, May 16, 2018

* Update akka-management version to `0.13`

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

### 1.5.0 - Wednesday, September 26, 2018

* Supports non-Alpine bases images.
* Upgraded automatic inclusion of reactive-lib to `0.9.2`.
* Automated RBAC setup for minikube deploy.

### 1.3.1 - Wednesday, July 18, 2018

* Upgraded automatic inclusion of reactive-lib to `0.9.0`.

### 1.3.0 - Monday, July 16, 2018

* Add `annotations` setting, a `Map` of keys and values that will be exported as labels on the Docker image.
  reactive-cli will use these to annotate DC/OS or Kubernetes resources.

### 1.2.3 - Tuesday, July 3, 2018

* Upgraded automatic inclusion of reactive-lib to `0.8.2`.

### 1.2.2 - Friday, June 22, 2018

* Add support for specifying user and group to be used for running the app inside Docker container

### 1.2.1 - Wednesday, May 16, 2018

* Make produced docker image application runnable by a non-root user.
* Upgraded automatic inclusion of reactive-lib to `0.8.1`.

### 1.2.0 - Wednesday, May 16, 2018

* Upgraded automatic inclusion of reactive-lib to `0.8.0`.

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

* Fixed a bug causing the plugin to fail on Lagom projects using sbt 1.0 or later.

### 0.6.0 - Monday, February 12, 2018

* Upgraded automatic inclusion of reactive-lib to `0.5.0`.
* Remove automatic setting of `dockerUsername`.
* Add a feature, `sbt 'deploy minikube'` that automatically deploys all projects into local Minikube
