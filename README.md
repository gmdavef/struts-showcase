# struts-showcase
The purpose of this repo is to provide example pipeline scripts for various CI/CD tools to illustrate scanning with the ReversingLabs CLI. Currently, scripts are provided for Jenkins, Azure DevOps, GitHub Actions, and TeamCity.

NOTE: This repo has the source code and Maven build instructions for the Struts2 showcase web app, which came with Apache Struts v2.5.28. This is arbitrary, because the ReversingLabs CLI is capable of scanning nearly any type of software artifact that results from a build.

## Jenkins

**Jenkinsfile**

A Jenkins pipeline script that builds the .war file, scans it with the [ReversingLabs CLI](https://docs.secure.software/cli/), and publishes the reports in JSON, CycloneDX, and SPDX formats. The HTML report is published and available under the "ReversingLabs Report" link in Jenkins. This script works for a scenario where the RL CLI has been installed on a dedicated/persistent Jenkins server. The inclusion of "--return-status" on the rl-secure status command causes an appropriate exit code to be returned (non-zero when scan fails policy or zero if scan passes policy).

**Jenkinsfile_docker**

A Jenkins pipeline script that builds the .war file and scans it using the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Reports in JSON, CycloneDX, and SPDX formats are saved as build artifacts. The HTML report is published and available under the "ReversingLabs Report" link in Jenkins. Using the Docker image is ideal for ephemeral instances of Jenkins. However, in this example, it's assumed you have a persistent Jenkins server with a local RL package store (note the Docker run command includes the --rl-store option that references the package store location). This allows for [policy configuration via .info files](https://docs.secure.software/cli/configuration/policy-configuration#policy-configuration-files). With an ephemeral instance of Jenkins, the RL package store would need to be elsewhere. Any persistent storage location (e.g., shared network drive, S3 bucket, etc.) would work.

## Azure DevOps

**azure-pipelines.yml**

An Azure DevOps pipeline script that builds the .war file and scans it with the ReversingLabs CLI. In this script, the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package is installed and subsequently used to install and license the CLI. Scan reports in HTML, JSON, CycloneDX, and SPDX formats are published as pipeline artifacts.

**azure-pipelines_docker.yml**

This pipeline script does the same thing as above, but doesn't install the CLI. Instead, it leverages the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner).  Scan reports in HTML, JSON, CycloneDX, and SPDX formats are published as pipeline artifacts.

## GitHub Actions

**rl-scan.yml**

A workflow, triggered manually only, that builds the .war file and scans it with the ReversingLabs CLI. The workflow installs the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package, which is subsequently used to install and license the CLI. Scan reports in HTML, JSON, CycloneDX, and SPDX formats are published as an artifact called "ReversingLabs reports".

**rl-scan-docker.yml**

This workflow does the same thing as above, but doesn't install the CLI. Instead, it leverages the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Scan reports in HTML, JSON, CycloneDX, and SPDX formats are published as an artifact called "ReversingLabs reports".

## TeamCity

**.teamcity/settings.kts**

This is a TeamCity project settings file that defines two stages ("Build" and "RL scan") to build the .war file and scan it using the [ReversingLabs Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Reports in HTML, JSON, CycloneDX, and SPDX formats are saved in a zip file artifact. With each build, the HTML report is published under a tab called "ReversingLabs Report" within TeamCity. Documentation for integrating the RL CLI with TeamCity can be found [here](https://docs.secure.software/cli/integrations/teamcity).
