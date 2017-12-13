# Deployment Setup

Deployment of your application requires the usage of a CLI tool, `rp`, provided by [`reactive-cli`](https://github.com/lightbend/reactive-cli). Typically, you'll want to install this on the same machine that you use `kubectl` from. A bastion host is recommended for production usage, but for local development this can be done on your own machine. 

## Install the CLI

### macOS

Releases for macOS are available using [Homebrew](https://brew.sh/). To set this up and install the CLI, use the command below.

```bash
brew tap lightbend/tools && brew install lightbend/tools/reactive-cli
```

To upgrade to a newer version of the CLI, use `brew upgrade`.

```bash
brew upgrade lightbend/tools/reactive-cli
```

To verify your installation:

```bash
rp version
```

### Debian & Ubuntu

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

To verify your installation:

```bash
rp version
```

### CentOS & RHEL

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

To verify your installation:

```bash
rp version
```
