# Installation

To use the platform tooling, you'll need to install the [Reactive CLI](https://github.com/lightbend/reactive-cli). Refer to the instructions for your platform below.

## Prerequisites

* [Docker](https://www.docker.com/)

## macOS

Releases for macOS are available using [Homebrew](https://brew.sh/). To set this up and install the CLI, use the command below.

```bash
brew tap lightbend/tools && brew install lightbend/tools/reactive-cli
```

To upgrade to a newer version of the CLI, use `brew upgrade`.

```bash
brew upgrade lightbend/tools/reactive-cli
```

## Debian & Ubuntu

Debian and Ubuntu `.deb` packages are available on Lightbend's [Debian Repository](https://bintray.com/lightbend/deb). To set this up and install the CLI, use the command below. Debian 8, Ubuntu 14.04, and later are supported

```bash
wget -qO - https://downloads.lightbend.com/rp/keys/bintray-debian | \
    sudo apt-key add - && \
    echo "deb https://dl.bintray.com/lightbend/deb $(lsb_release -cs) main" | \
    sudo tee /etc/apt/sources.list.d/lightbend.list && \
    sudo apt-get update && \
    sudo apt-get install reactive-cli
```

To upgrade to a newer version of the CLI, you can use `apt-get`.

```bash
sudo apt-get update && sudo apt-get install reactive-cli
```

## CentOS & RHEL

CentOS and RHEL `.rpm` packages are available on Lightbend's [Yum Repository](https://bintray.com/lightbend/rpm). To set this up and install the CLI, use the command below. CentOS 6, RHEL 6, and later are supported.

```bash
wget -qO - https://bintray.com/lightbend/rpm/rpm | \
    sudo tee /etc/yum.repos.d/bintray-lightbend-rpm.repo && \
    sudo yum install reactive-cli
```

Similarly, use `yum` to update your installation.

```bash
sudo yum update reactive-cli
```