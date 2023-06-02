# struts-showcase
This repo contains the code for the Struts2 showcase web app (v2.5.28) and includes several CI/CD scripts to demonstrate scanning the WAR artifact with ReversingLabs.

**Jenkinsfile**

A Jenkins pipeline script that builds the .war file, scans it with the [ReversingLabs CLI](https://docs.secure.software/cli/), and publishes the reports in HTML, JSON, and CycloneDX formats. This script is for when the RL CLI has been installed on a Jenkins build server.

**Jenkinsfile_docker**

A Jenkins pipeline script that builds the .war file, scans it using the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner), and publishes the reports in HTML, JSON, and CycloneDX formats. Using the Docker image is ideal for an ephemeral instance of Jenkins.

**azure-pipelines.yml**

An Azure DevOps pipeline script that builds the .war file and scans it with the ReversingLabs CLI. In this script, the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package is installed and subsequently used to install and license the CLI. Scan reports in HTML, JSON, and CycloneDX formats are published as pipeline artifacts.