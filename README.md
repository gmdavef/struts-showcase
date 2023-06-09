# struts-showcase
This repo contains the code and Maven build instructions for the Struts2 showcase web app v2.5.28. It also includes several scripts to demonstrate scanning the build artifact with ReversingLabs in different CI/CD tools.

## Jenkins

**Jenkinsfile**

A Jenkins pipeline script that builds the .war file, scans it with the [ReversingLabs CLI](https://docs.secure.software/cli/), and publishes the reports in HTML, JSON, and CycloneDX formats. This script works for a scenario where the RL CLI has been installed on a dedicated/persistent Jenkins server.

**Jenkinsfile_docker**

A Jenkins pipeline script that builds the .war file, scans it using the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner), and publishes the reports in HTML, JSON, and CycloneDX formats. Using the Docker image is ideal for an ephemeral instance of Jenkins.

## Azure DevOps

**azure-pipelines.yml**

An Azure DevOps pipeline script that builds the .war file and scans it with the ReversingLabs CLI. In this script, the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package is installed and subsequently used to install and license the CLI. Scan reports in HTML, JSON, and CycloneDX formats are published as pipeline artifacts.

**azure-pipelines_docker.yml**

This pipeline script does the same thing as above, but doesn't install the CLI. Instead, it leverages the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner).  Scan reports in HTML, JSON, and CycloneDX formats are published as pipeline artifacts.

## GitHub Actions

**rl-scan.yml**

A workflow, triggered manually only, that builds the .war file and scans it with the ReversingLabs CLI. The workflow installs the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package, which is subsequently used to install and license the CLI. Scan reports in HTML, JSON, and CycloneDX formats are published as an artifact called "ReversingLabs reports".

**rl-scan-docker.yml**

This workflow does the same thing as above, but doesn't install the CLI. Instead, it leverages the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Scan reports in HTML, JSON, and CycloneDX formats are published as an artifact called "ReversingLabs reports".
