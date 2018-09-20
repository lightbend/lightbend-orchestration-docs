## Akka Cluster Bootstrap

<link rel="stylesheet" type="text/css" href="../css/custom.css">

@@@ note
Integration with all deployment targets except for Minikube is **Incubating** at this point.
@@@

[Akka Cluster Bootstrap][akka-cluster-bootstrap] is an extension module to Akka that allows an Akka Cluster to (semi) automatically discover its neighbouring nodes, and join the existing cluster, or safely form a new cluster for discovered nodes.

When enabled from Lightbend Orchestration, this feature will use service discovery to identify other contact points. Once the requisite number of
contact points have been identified, the one with the lowest address will form the cluster. The other nodes will then join that one. See the [Akka Cluster Bootstrap][akka-cluster-bootstrap] documentation for details.

This feature is automatically enabled for Lagom applications that require it. It can be manually enabled with the following build configuration:

```sbt
enableAkkaClusterBootstrap := true
```

When you generate resources for a deployment, you'll need to specify a replica count greater than 2. A value of 3 or
5 is recommended. This is to reduce the risk of multiple islands of clusters being formed when first formed. For example,
the following command specifies 5 replicas which are used as the contact points for forming the cluster:

```bash
rp generate-kubernetes-resources my-org/my-app:0.1.0 --pod-controller-replicas 5
```

By default, the feature will be configured to communicate and join other applications with the same name. If you have two disparate applications
that should join the same cluster, use the `akkaClusterBootstrapSystemName` setting. For example, all applications that specify the following
will join the same cluster, even if they have different names:

```sbt
akkaClusterBootstrapSystemName := "my-actor-system"
```

You can optionally use the --join-existing-akka-cluster flag to make sure new nodes will only join an existing cluster, and not form a new one themselves.
This can be useful if you expect the cluster to never be fully down. It's also quite useful for launching one-off processes or jobs that
are intended to join an already bootstrapped cluster.

```bash
rp generate-kubernetes-resources my-org/my-app:0.1.0 --join-existing-akka-cluster
```

When deploying applications that use Akka Cluster, you'll typically want them to join the same cluster. This is especially true if you
use Akka Persistence features. Because of this, it is recommended that you avoid Blue/Green deployments and instead use
the Canary (default) or Rolling deployment types

  [akka-cluster-bootstrap]: https://developer.lightbend.com/docs/akka-management/current/bootstrap/
