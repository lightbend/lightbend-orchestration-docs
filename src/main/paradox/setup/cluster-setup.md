# Cluster Setup

## Kubernetes

<link rel="stylesheet" type="text/css" href="css/custom.css">

@@@ note
Integration with Kubernetes except for Minikube is **Incubating** at this point.
@@@

When deploying to Kubernetes, you'll first need to setup your Kubernetes cluster. Below, you’ll find information on how to do this on your own local cluster, Minikube, as well as IBM's Cloud offerings. If you have access to a different Kubernetes environment, ensure that you’ve setup `kubectl` and `docker` to point at your cluster and docker registry.

### Minikube

[Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) provides a way for you to run a local Kubernetes cluster for development purposes. The command below will reset your Minikube and ensure that `kubectl` and `docker` can communicate with it.

#### Reset your Minikube (Optional)

If you wish to start with a fresh Minikube, run the command below. This will remove your existing Minikube and all of its data.

```bash
minikube delete
```

#### Start Minikube

macOS / Linux
:  ```bash
    minikube start
    eval $(minikube docker-env)
    ```

Windows
:  ```powershell
    minikube start
    minikube docker-env | Invoke-Expression
```

### IBM Cloud

[IBM Cloud](https://www.ibm.com/cloud/) offers Kubernetes clusters that can be used in production environments. To use your IBM Cloud cluster, follow the instructions on their website. You'll need to setup both the [Container Service](https://www.ibm.com/cloud/container-service) and the [Container Registry](https://www.ibm.com/cloud/container-registry). The IBM Cloud console will guide you through creating a cluster, installing the `bx` tool, and using that to configure `kubectl`.

### IBM Cloud Private

[IBM Cloud Private](https://www.ibm.com/cloud-computing/products/ibm-cloud-private/) is an on-prem deployment of IBM Cloud. To deploy to your IBM Cloud Private cluster, you’ll need a working deployment of IBM Cloud Private and access to a Docker Registry.

---------------------------------

Once you’ve configured your Kubernetes environment, you should be able to verify access with the following command:

```bash
kubectl get nodes
```

## DC/OS

@@@ note
Integration with DC/OS is **Incubating** at this point.
@@@

When deploying to DC/OS, you'll need to make sure that the `dcos` command line application is on your `PATH` and setup to point to your DC/OS cluster.

## DC/OS Vagrant

[DC/OS Vagrant](https://github.com/dcos/dcos-vagrant) can be used to quickly provision a DC/OS cluster on a local machine. Consult its README to get started.

---------------------------------

Once you've configured your DC/OS environment, you should be able to verify access with the following command:

```bash
dcos node
```
