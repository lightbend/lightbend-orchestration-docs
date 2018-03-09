# Examples

We've prepared sample applications using Lightbend Orchestration for Kubernetes to serve as a reference. Refer to the list below for more details.

### Hello World

[Hello-World](https://github.com/fsat/hello-reactive-tooling) is a bare-bones project that provides a Play service and a Lagom service. It shows how the two are connected using service discovery.

### Chirper

[Chirper](https://github.com/longshorej/lagom-java-chirper-tooling-example) is a Twitter-clone built using the [Lagom](https://www.lagomframework.com/) framework. Consult its `README.md` for information on how it can be deployed to Kubernetes.

> Note: This is a fork that has had all deployment-related resources (and the Maven build) removed to make it as easy as possible to understand the process.

### Akka Cluster Tooling Example

[Akka Cluster Tooling Example](https://github.com/longshorej/akka-cluster-tooling-example) is a bare-bones Akka Cluster and HTTP application that illustrates the steps involved to take advantage of Lightbend Orchestration for Kubernetes.

### Online Auction

[Online Auction](https://github.com/lagom/online-auction-scala) is an implementation of an auction website using Lagom. It is comprised of 5 microservices and a front end. Third-party technology includes Kafka, Cassandra, Elasticsearch, and ZooKeeper (for Kafka). Be sure to consult its `DEPLOY.md` file for steps on how you can deploy it to Kubernetes.
