## Docker and JVM Configuration

@@include[deprecation.md](deprecation.md)

The Docker image is the deployment unit of choice and powers a wide variety of orchestration systems, including Kubernetes and DC/OS. The docker images created by Lightbend Orchestration offer the following default configuration:

1. Minimal base-image, `openjdk:alpine`, to reduce the size of the images produced.
2. JVM's Docker CPU and memory limits are enabled, as discussed on this Oracle [Blog Post](https://blogs.oracle.com/java-platform-group/java-se-support-for-docker-cpu-and-memory-limits).

For sbt-reactive-app 1.5.0 and later, `rpPackagingDockerCommmands` setting can be used to append either additonal packages or to build on an alternative base image.

### Additional Alpine packages

@@snip [build.sbt](../../../sbt-test/orchestration/alpine/build.sbt) { #alpine_example }

### Debian slim example

@@snip [build.sbt](../../../sbt-test/orchestration/slim/build.sbt) { #slim_example }
