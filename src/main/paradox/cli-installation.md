# CLI Installation

Deployment of your application requires the usage of a CLI tool, `rp`, provided by [`reactive-cli`](https://github.com/lightbend/reactive-cli). Typically, you'll want to install this on the same machine that you use `kubectl` or `dcos` from. A bastion host is recommended for production usage, but for local development this can be done on your own machine.

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

If you don't have `jq` you must also install that: `brew install jq`.

### Debian & Ubuntu

Debian and Ubuntu `.deb` packages are available on Lightbend's [Debian Repository](https://bintray.com/lightbend/deb). To set this up and install the CLI, use the command below. Debian 8, Ubuntu 14.04, and later are supported

```bash
wget -qO - https://downloads.lightbend.com/rp/keys/bintray-debian | \
    sudo apt-key add - && \
    echo "deb https://dl.bintray.com/lightbend/deb $(lsb_release -cs) main" | \
    sudo tee /etc/apt/sources.list.d/lightbend.list && \
    sudo apt-get install apt-transport-https -y && \
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

### Linux (Other)

Linux (generic `.tar.gz` file) packages are available on Lightbend's [Bintray Repository](https://bintray.com/lightbend/generic/reactive-cli).
To install, download the latest version (Available under the *Files* tab for a given version), extract it, and ensure that `rp` is available on your `PATH`.

### Windows

Windows packages are available on Lightbend's [Bintray Repository](https://bintray.com/lightbend/generic/reactive-cli). To install,
download the latest version (Available under the *Files* tab for a given version) and extract it.

Ensure that `rp.exe` is on your PATH. A quick way to do this from PowerShell is to `cd` where `rp.exe` is located and run this:

```powershell
$env:Path += ";" + $pwd.Path
If (-not (Test-Path $Profile)) {New-Item $profile -itemtype file}
Write-Output ('$env:Path += ";" + ''' + $pwd.Path + "'") | Out-File $profile -Append
```

#### Removing the rp PowerShell Alias

Within PowerShell, you must invoke `rp.exe` since `rp` refers to an alias. If you prefer, you may remove that alias from the current and subsequent shells:

```powershell
Remove-Item alias:\rp -Force
If (-not (Test-Path $Profile)) {New-Item $profile -itemtype file}
echo "Remove-Item alias:\rp -Force" | Out-File $profile -Append
```

## Private Docker Registries

Docker images you build using sbt-reactive-app plugin will need to be accessed by the CLI. Depending on where you put them, CLI might need your authentication credentials to be able to access the registry. It will try to read credentials stored locally on your system by docker after you authenticate:

```bash
docker login my-docker-registry.bintray.io
```
When reading these credentials CLI might prompt to enter your user password, since the data is stored in a secure OS-specific enclave. If you don't want this, it is possible to explicitly provide your credentials by writing them down to `~/.lightbend/docker.credentials` (Linux, macOS) or `%HOMEPATH%\.lightbend\docker.credentials` (Windows) file:

```
registry = my-docker-registry.bintray.io
username = foo
password = bar

registry = my-docker-registry2.bintray.io
username = foo2
password = bar2
```
