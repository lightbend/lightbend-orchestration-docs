# Building & Publishing

## SBT

Once you've setup your build, you'll need to create Docker images and optionally publish them to a Docker Registry.

### Building

To build your application locally, you'll need to run the following command from the SBT shell:

```scala
sbt> docker:publishLocal
```

> Using [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/)? Be sure to run `eval $(minikube docker-env)` prior to launching SBT. This will ensure that the images are built inside your Minikube cluster without having to use a registry. `docker images` from within the same shell can help you inspect the images you've built. When you need your old environment, you can get it back with `eval $(minikube docker-env -u)`. 

### Publishing to a Docker Registry

To publish your applications to a Docker Registry, you'll need to configure your build and authenticate to the registry.

1. For each project, set the `dockerRepository` SBT Setting. For example:
```scala
dockerRepository := "my-docker-registry.bintray.io"
```
2. Authenticate to that registry, for example:
```bash
docker login my-docker-registry.bintray.io
```
3. Push your images to that registry:
```scala
sbt> docker:publish
```

> Need help making releases? We recommend using the excellent [sbt-release](https://github.com/sbt/sbt-release) which can guide you through the process of incrementing version numbers and publishing releases.