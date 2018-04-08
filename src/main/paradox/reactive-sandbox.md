# Reactive Sandbox

Lightbend Orchestration for Kubernetes is integrated with Lightbend's [Reactive Sandbox](https://github.com/lightbend/reactive-sandbox)
which provides development-grade (i.e. don't use it in production) installations of Cassandra, Elasticsearch, Kafka, and ZooKeeper.

You can install the sandbox with the commands below. If you use the `deploy minikube` sbt task, it will automatically
install it for you if required. You can also use the setting `deployMinikubeEnableReactiveSandbox` to adjust this
behavior.

```bash
# Install Helm and add the Lightbend repository
helm init
helm repo add lightbend-helm-charts https://lightbend.github.io/helm-charts
helm update

# Install the reactive-sandbox
helm install lightbend-helm-charts/reactive-sandbox --name reactive-sandbox
```

Be sure to consult the project's [README.md](https://github.com/lightbend/reactive-sandbox/blob/master/README.md) for
additional features and settings.
