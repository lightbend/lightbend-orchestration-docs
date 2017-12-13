# Overview

The Platform Tooling suite provides an easy way to create Docker images for Lightbend Reactive Platform applications. It can be used to ease deployment to Kubernetes by automatically generating resource files for you and reducing the friction between development and operations.

## Components

The tooling suite consists of three main parts:
 
* An SBT Plugin, [sbt-reactive-app](https://github.com/lightbend/sbt-reactive-app), which inspects your projects and builds annotated [Docker](https://www.docker.com/) Images.
* A command-line tool, [reactive-cli](https://github.com/lightbend/reactive-cli), which generates resources for [Kubernetes](https://kubernetes.io/).
* An application library, [reactive-lib](https://github.com/lightbend/reactive-lib/), which inspects its runtime environment to ensure that your application is configured correctly. It provides easy-to-use APIs for concepts such as service discovery, cluster formation, secrets access, and port binding. This library is automatically included in your project and configured by `sbt-reactive-app`.

By using these tools, you can leverage the metadata that already exists in your project to easily deploy to Kubernetes without having to manually craft any JSON or YAML files.

## Features

**Akka Cluster Formation**

A service discovery-based approach to Akka Cluster formation that allows you to safely and efficiently form clusters in orchestrated environments. For more information on this works, be sure to consult the [Akka Cluster Bootstrap](https://developer.lightbend.com/docs/akka-management/current/bootstrap.html) documentation.

**Endpoint Detection & Declaration**

Microservices are detected and HTTP endpoints are automatically declared for you based on the service name. For Kubernetes, this translates to the generation of `Service` and `Ingress` resources. Additional endpoints can be declared manually as well. Port declaration and configuration for these endpoints is automatically handled when possible but APIs are provided to calculate the correct host and port if your project manually performs the socket binding.

**Docker & JVM Configuration**

The Docker images produced by the tools are configured out of the box to use a lightweight [Alpine Linux](https://alpinelinux.org/) based Docker image. Additionally, if you've declared a memory limit your project will automatically enable the appropriate CGroup JVM options.

**Secrets API**

A simple asynchronous secrets API is provided. Your application simply needs to define the name and key of secrets it is interested in, and then it can use the provided libraries to access the secret values at runtime. This simplifies secret usage as the developer doesn't need to worry about where secrets are; this information is generated for them. 

**Service Location**

A Service Location facility is provided that understands the conventions produced by the tooling and uses them to locate other services. This means you can rely on service discovery to "just work."

## Supported Platforms

Currently, the tooling helps you leverage these features on Kubernetes. Support for other platforms is being considered.