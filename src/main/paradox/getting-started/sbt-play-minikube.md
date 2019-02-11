# sbt Example: Play on Minikube

### Prerequisites

* [reactive-cli](../setup/cli-installation.html) installed
* Java
* sbt

### Install Docker CE

Follow the instruction on [About Docker CE][docker-ce] to install Docker.

### Install kubectl

Follow the instruction on [Install and Set Up kubectl][install-kubectl].

### Install Minikube

Follow the instruction on [Minikube][minikube] repository to install it. For macOS it's:

```bash
brew cask install minikube
```

### Confirm installation

```bash
docker --version

Docker version 18.03.1-ce, build 9ee9f40

kubectl version

Client Version: version.Info{Major:"1", Minor:"9", GitVersion:"v1.9.3", GitCommit:"d2835416544f298c919e2ead3be3d0864b52323b", GitTreeState:"clean", BuildDate:"2018-02-09T21:51:54Z", GoVersion:"go1.9.4", Compiler:"gc", Platform:"darwin/amd64"}
Server Version: version.Info{Major:"1", Minor:"10", GitVersion:"v1.10.0", GitCommit:"fc32d2f3698e36b93322a3465f63a14e9f0eaead", GitTreeState:"clean", BuildDate:"2018-03-26T16:44:10Z", GoVersion:"go1.9.3", Compiler:"gc", Platform:"linux/amd64"}

minikube version

minikube version: v0.28.0
```

### Create a new Play application

```bash
sbt new playframework/play-scala-seed.g8

This template generates a Play Scala project

name [play-scala-seed]: hello-play
organization [com.example]:
play_version [2.6.16]:
sbt_version [1.1.6]:
scalatestplusplay_version [3.1.2]:

Template applied in ./hello-play
```

### Project setup

Add the sbt-reactive-app plugin to your project's `project/plugins.sbt` file:

@@@vars
```scala
addSbtPlugin("com.lightbend.rp" % "sbt-reactive-app" % "$sbtreactiveapp$")
```
@@@

Edit `build.sbt` to include `SbtReactiveAppPlugin`:

```scala
lazy val root = (project in file("."))
  .enablePlugins(PlayScala, SbtReactiveAppPlugin)
```

### Declare endpoint

Add the following to `build.sbt` to declare an endpoint:

```scala
rpEndpoints := HttpEndpoint(
  name = "http",
  ingress = new HttpIngress(
    ingressPorts = Vector(80, 443),
    hosts = Vector.empty,
    paths = Vector("/")
  )
) :: Nil
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

### Deploy to Minikube

Within the same terminal window, start the sbt shell:

```
sbt
```

In the sbt shell type the following:

```
compile
rpDeploy minikube
```

If successful you should see an output like the following:

```
[info] Sending build context to Docker daemon  40.64MB
[info] Step 1/11 : FROM openjdk:8-jre-alpine
[info]  ---> ccfb0c83b2fe
[info] Step 2/11 : RUN /sbin/apk add --no-cache bash
[info]  ---> Using cache
[info]  ---> a7c9d4ccf2bb
[info] Step 3/11 : RUN id -g daemon || addgroup daemon
[info]  ---> Using cache
[info]  ---> 85158e8f22cc
[info] Step 4/11 : RUN id -u daemon || adduser daemon daemon
[info]  ---> Using cache
[info]  ---> 25cd7385ab4c
[info] Step 5/11 : WORKDIR /opt/docker
[info]  ---> Using cache
[info]  ---> bc6a3c77f060
[info] Step 6/11 : ADD --chown=daemon:daemon opt /opt
[info]  ---> Using cache
[info]  ---> aa5a2b6b1aef
[info] Step 7/11 : USER daemon
[info]  ---> Using cache
[info]  ---> 1007538ab33a
[info] Step 8/11 : ENTRYPOINT []
[info]  ---> Using cache
[info]  ---> 67a009f36ddd
[info] Step 9/11 : CMD []
[info]  ---> Using cache
[info]  ---> 6f9152dfd589
[info] Step 10/11 : COPY --chown=daemon:daemon rp-start /rp-start
[info]  ---> Using cache
[info]  ---> 388b9495fa0b
[info] Step 11/11 : LABEL com.lightbend.rp.app-name="hello-play" com.lightbend.rp.applications.0.name="default" com.lightbend.rp.applications.0.arguments.0="/rp-start" com.lightbend.rp.applications.0.arguments.1="bin/hello-play" com.lightbend.rp.app-version="1.0-SNAPSHOT" com.lightbend.rp.app-type="play" com.lightbend.rp.config-resource="rp-application.conf" com.lightbend.rp.modules.akka-cluster-bootstrapping.enabled="false" com.lightbend.rp.modules.akka-management.enabled="false" com.lightbend.rp.modules.common.enabled="true" com.lightbend.rp.modules.play-http-binding.enabled="true" com.lightbend.rp.modules.secrets.enabled="false" com.lightbend.rp.modules.service-discovery.enabled="false" com.lightbend.rp.modules.status.enabled="false" com.lightbend.rp.endpoints.0.name="http" com.lightbend.rp.endpoints.0.protocol="http" com.lightbend.rp.endpoints.0.ingress.0.type="http" com.lightbend.rp.endpoints.0.ingress.0.ingress-ports.0="80" com.lightbend.rp.endpoints.0.ingress.0.ingress-ports.1="443" com.lightbend.rp.endpoints.0.ingress.0.paths.0="/" com.lightbend.rp.sbt-reactive-app-version="1.3.1"
[info]  ---> Using cache
[info]  ---> c44b60fc24d7
[info] Successfully built c44b60fc24d7
[info] Successfully tagged hello-play:1.0-SNAPSHOT
[info] Built image hello-play:1.0-SNAPSHOT
[info] deployment "hello-play-v1-0-snapshot" deleted
[info] service "hello-play" deleted
[info] ingress "hello-play" deleted
[info] deployment "hello-play-v1-0-snapshot" created
[info] service "hello-play" created
[info] ingress "hello-play" created
```

Exit sbt shell:

```
exit
```

### Confirm Play application

Type `minikube ip` to find out the IP address of Minikube:

```bash
minikube ip

192.168.99.101
```

Open the IP address in your browser, and say yes to ignoring the certificate not matching up.

<img src="../files/welcome.png" class="small-12 float-center">

Now you have Play application running on Minikube.

  [minikube]: https://github.com/kubernetes/minikube
  [install-kubectl]: https://kubernetes.io/docs/tasks/tools/install-kubectl/
  [docker-ce]: https://docs.docker.com/install/
