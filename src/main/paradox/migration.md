# Migrating to the Improved Kubernetes Deployment Experience

Lightbend released Lightbend Orchestration as an [Incubating](https://developer.lightbend.com/docs/reactive-platform/2.0/support-terminology/index.html#incubating) open source technology preview to help our users deploy their Reactive Microservices to Kubernetes and DC/OS (Marathon) environments. Since then, we've seen adoption of Kubernetes skyrocket, and many users of Akka, Lagom and Play have tried Lightbend Orchestration as they ventured into this world. While many of them have been successfully deploying their services using Lightbend Orchestration, we've received feedback from customers and other users that the tooling provided by Lightbend Orchestration is insufficiently flexible and can be difficult to integrate with existing Kubernetes workflows and the exploding number of third-party tools built for Kubernetes.

After evaluating several options, we've decided to integrate the most popular features of Lightbend Orchestration natively into Akka and Lagom, and stop development of Lightbend Orchestration as a separate project after version 1.7. We see this as a natural evolution that reflects the move to more environmentally aware features in development frameworks and tools today.  Previously-released versions will continue to be available, but Akka Management 1.0.0 and Lagom 1.5.0 and later are no longer compatible with Lightbend Orchestration, as they have now integrated and improved upon some of its features.


Many of the features that were originally [Incubating](https://developer.lightbend.com/docs/reactive-platform/2.0/support-terminology/index.html#incubating) in Lightbend Orchestration have been improved and migrated to the [Akka Management](https://developer.lightbend.com/docs/akka-management/current/) and [Lagom](https://www.lagomframework.com/) projects. We recommend transitioning services built using Lightbend Orchestration to the fully [Supported](https://developer.lightbend.com/docs/reactive-platform/2.0/support-terminology/index.html#supported) replacements outlined below. Lagom users updating to version 1.5 can find more detailed instructions in the [Lagom 1.5 Migration Guide](https://www.lagomframework.com/documentation/1.5.x/scala/Migration15.html#Lightbend-Orchestration). You can find a complete example of deploying a Lagom service to OpenShift --- Red Hat's distribution of Kubernetes --- in our new guide: ["Deploying Lightbend applications to OpenShift"](https://developer.lightbend.com/guides/openshift-deployment/).

## Service Discovery

Service Discovery is now provided by the new [Akka Discovery](https://doc.akka.io/docs/akka/current/discovery/) API. Most Kubernetes and Marathon users should configure the [DNS Discovery Method](https://doc.akka.io/docs/akka/current/discovery/#discovery-method-dns) as the default service discovery implementation, to make use of each platform's native support for DNS SRV with a custom, high-performance, asynchronous DNS implementation. Those needing manual configuration can use the [Configuration Discovery Method](https://doc.akka.io/docs/akka/current/discovery/#discovery-method-configuration), and both can be used together by configuring the [Aggregate Discovery Method](https://doc.akka.io/docs/akka/current/discovery/#discovery-method-aggregate-multiple-discovery-methods).

Akka Discovery is available to all users of Akka 2.5.19 or later, including users of Play or Lagom. Lagom users can use the [Lagom Akka Discovery ServiceLocator](https://github.com/lagom/lagom-akka-discovery-service-locator) project for seamless integration. It is compatible with Lagom 1.4 and 1.5.

## Cluster Bootstrap

Akka Cluster users, including those using Lagom in a clustered configuration, can use [Akka Cluster Bootstrap](https://developer.lightbend.com/docs/akka-management/current/bootstrap/) directly in their services. This is the underlying technology that Lightbend Orchestration used for cluster bootstrapping, but with further improvements and now fully Supported part of the Lightbend Platform as of version 1.0.0 when used with Kubernetes.

Lagom 1.5 [integrates Akka Cluster Bootstrap](https://www.lagomframework.com/documentation/1.5.x/scala/Cluster.html#Joining-during-production-%28Akka-Cluster-Bootstrap%29) and starts it automatically. Lagom 1.4 services can [invoke it explicitly](https://developer.lightbend.com/docs/akka-management/current/bootstrap/#using).

Akka Cluster Bootstrap builds on Akka Discovery, and we recommend setting the `akka.management.cluster.bootstrap.contact-point-discovery.discovery-method` property to the implementation native to your deployment platform: [Kubernetes API](https://developer.lightbend.com/docs/akka-management/current/bootstrap/kubernetes-api.html) or [Marathon API](https://developer.lightbend.com/docs/akka-management/current/discovery/marathon.html).

## Health Checks

Akka Management now provides built-in [health checks](https://developer.lightbend.com/docs/akka-management/current/healthchecks.html). Lagom 1.5 [integrates Akka Management Health checks](https://www.lagomframework.com/documentation/1.5.x/scala/Cluster.html#Health-Checks) automatically and enables them by default. Users of earlier Lagom versions, Akka HTTP, or Play can configure these manually by following the instructions in the Akka Management documentation.

## Docker image builds

Projects that used Lightbend Orchestration with sbt to build Docker images can switch to the popular [SBT Native Packager](https://sbt-native-packager.readthedocs.io/en/latest/) plugin that was used as its underlying implementation. This plugin is already available in all sbt-based Play and Lagom projects and offers highly customizable support for building [Docker images](https://sbt-native-packager.readthedocs.io/en/latest/formats/docker.html). You won't get the additional Docker image labels that were added automatically by the sbt-reactive-app component of Lightbend Orchestration, but these can be added back if desired by configuring the [`dockerLabels` setting](https://sbt-native-packager.readthedocs.io/en/latest/formats/docker.html#environment-settings).

Alternatively, you can use any other method you choose to build Docker images for your services --- there are many third-party tools available in the ecosystem.

## Other features

Other features, including generation of Kubernetes and Marathon configuration files using the `rp` command-line tool, have no direct replacement. Instead, we've found that most users benefit from managing these files directly, allowing them to take advantage of the full flexibility of their target deployment platform without the layer of indirection or need for extra custom tools. Those with more advanced needs can take advantage of a wide variety of tools that have been developed within the Kubernetes ecosystem for managing deployment configuration files.

## List of deprecated components

Lightbend Orchestration consisted of these components, all of which are now deprecated:

- sbt-reactive-app --- sbt plugin
- reactive-cli --- rp command-line tool
- reactive-lib --- runtime library, automatically added as a dependency by sbt-reactive-app
- reactive-sandbox --- Helm charts for installing Cassandra, Elasticsearch, Kafka and ZooKeeper

We at Lightbend thank all of you who tested and provided feedback on Lightbend Orchestration. We will be happy to provide support to Lightbend Platform subscribers who would like assistance with the migration to our new recommended approach --- please [contact Lightbend Support](https://support.lightbend.com/customer/portal/emails/new) with any questions or feedback. We also invite community users to ask questions and give us feedback on the public [Lightbend Discourse forums](https://discuss.lightbend.com/).
