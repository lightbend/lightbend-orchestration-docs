## Docker and JVM Configuration

The Docker image is the deployment unit of choice and powers a wide variety of orchestration systems, including Kubernetes and DC/OS. The docker images created by Lightbend Orchestration offer the following default configuration:

1. Minimal base-image, `openjdk:alpine`, to reduce the size of the images produced.
2. JVM's Docker CPU and memory limits are enabled, as discussed on this Oracle [Blog Post](https://blogs.oracle.com/java-platform-group/java-se-support-for-docker-cpu-and-memory-limits).

Additionally, a convenience setting is provided, `alpinePackages`, which can be used to specify additional Alpine packages that should be installed when building the Docker image. For example, the following build configuration ensures `core-utils` is added to the Docker image:

```sbt
alpinePackages += "coreutils"
```
