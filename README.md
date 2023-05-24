# struts-showcase
Struts2 showcase web app (v2.5.28) including various CI/CD scripts to scan the .war build artifact with ReversingLabs.

**Jenkinsfile**

A Jenkins pipeline script that builds the WAR, scans it with the [ReversingLabs CLI](https://docs.secure.software/cli/), and publishes the reports in HTML, JSON, and CycloneDX formats. This script is for a setup where the RL CLI has been installed on a persistent instance of Jenkins.

**Jenkinsfile_docker**

A Jenkins pipeline script that builds the WAR, scans it using the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner), and publishes the reports in HTML, JSON, and CycloneDX formats. Using the Docker image is ideal for an emphemeral instance of Jenkins.

**azure_pipelines.yml**

An Azure DevOps pipeline script that builds the WAR, scans it with the ReversingLabs CLI, and publishes the reports in HTML, JSON, and CycloneDX format. Here, the [rl-deploy Python package](https://pypi.org/project/rl-deploy/) is installed and subsequently used to install and license the CLI on the fly. Scan reports are publishes in HTML, JSON, and CycloneDX formats.