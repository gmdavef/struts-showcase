# struts-showcase
The purpose of this repo is to provide example pipeline scripts for various CI/CD tools to illustrate scanning with the ReversingLabs Spectra Assure software supply chain security solution. Currently, scripts are provided for Jenkins, Azure DevOps, GitHub Actions, TeamCity, and GitLab.

NOTE: This repo contains the source code and Maven build instructions (pom.xml) for the Struts2 showcase web app. This web app was released with Apache Struts v2.5.28. This choice is somewhat arbitrary, because Spectra Assure is capable of scanning nearly any type of software artifact that results from a build.

## Jenkins Examples

**Jenkinsfile**

A Jenkins pipeline script that builds the .war file, scans it with the [Spectra Assure CLI](https://docs.secure.software/cli/), and stores the reports in RL-JSON, CycloneDX, and SPDX formats as build artifacts. The HTML report is published and available under the "ReversingLabs Report" link in Jenkins. This script works for a scenario where the CLI has been installed on a dedicated/persistent Jenkins server. The inclusion of "--return-status" on the rl-secure status command causes an appropriate exit code to be returned (non-zero when scan fails policy or zero if scan passes policy).

**Jenkinsfile_diff**

A Jenkins pipeline script just like the above, but a differential report is generated to compare results against the previous build. With this, an extra "Diff" tab appears in the HTML report. Analyzing the diff is helpful to spot potential malware in your release.

**Jenkinsfile_proxy**

A pipeline script similar to the above, but a different instance of the CLI is used that's configured to connect to the cloud via a proxy. Proxy details are stored in the config.info file at the root of the package store. The same reports are generated and made available. 

**Jenkinsfile_docker**

A Jenkins pipeline script that builds the .war file and scans it with the CLI using the [Spectra Assure Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Reports in RL-JSON, CycloneDX, and SPDX formats are saved as build artifacts. The HTML report is published and available under the "ReversingLabs Report" link in Jenkins. Using the Docker image is ideal for ephemeral instances of Jenkins. However, in this example, it's assumed you have a persistent Jenkins server with a local RL package store (note the Docker run command includes the --rl-store option that references the package store location). This allows for [policy configuration via .info files](https://docs.secure.software/cli/configuration/policy-configuration#policy-configuration-files). With an ephemeral instance of Jenkins, the RL package store would need to be elsewhere. Any persistent storage location (e.g., shared network drive, S3 bucket, etc.) would work.

**Jenkinsfile_docker_proxy**

A Jenkins pipeline script just like the above, but the Docker run command includes proxy settings so that the CLI running in the container connects to the RL cloud via a proxy.

**Jenkinsfile_docker_cloudscan_trial**

A Jenkins pipeline script that builds the .war file and uploads it for scanning using the [Spectra Assure Cloud Scan Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner-cloud). Here, the scan happens in the Spectra Assure cloud portal (the "Trial" instance to be exact). The "--submit-only" option can be used so that the pipeline proceeds without waiting for the scan to finish and the stage will pass regardless of scan results. If it's decided the pipeline should wait for the scan to complete, the stage may pass or fail depending on the results and reports in RL-JSON, SARIF, CycloneDX, and SPDX formats can be downloaded and saved as build artifacts. Either way, the HTML report is available in the Spectra Assure cloud portal.

## Azure DevOps Examples

**azure-pipelines.yml**

An Azure DevOps pipeline script that builds the .war file and scans it locally with the ReversingLabs Spectra Assure CLI. In this script, the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package is installed and subsequently used to install and license the CLI. Scan reports in HTML, RL-JSON, CycloneDX, and SPDX formats are published as pipeline artifacts.

**azure-pipelines_docker.yml**

This pipeline script does the same thing as above, but instead leverages the [Spectra Assure Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Scan reports in HTML, RL-JSON, CycloneDX, and SPDX formats are published as pipeline artifacts.

**azure-pipelines_docker_cloudscan_trial.yml**

This pipeline script builds the .war file and uploads it for scanning using the [Spectra Assure Cloud Scan Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner-cloud). Here, the scan happens in the Spectra Assure cloud portal (the "Trial" instance to be exact). The "--submit-only" option can be used so that the pipeline proceeds without waiting for the scan to finish and the stage will pass regardless of scan results. If it's decided the pipeline should wait for the scan to complete, the stage may pass or fail depending on the results and reports in RL-JSON, SARIF, CycloneDX, and SPDX formats can be downloaded and saved as build artifacts. Either way, the HTML report is available in the Spectra Assure cloud portal.

**azure-pipelines_ado_extension.yml**

This pipeline script builds the .war file and scans it locally using the [rl-scanner-task Azure DevOps extension](https://marketplace.visualstudio.com/items?itemName=ReversingLabs.rl-scanner-task), which is published to the Azure DevOps Marketplace. Make sure to install the extension before using it. The extension leverages the Spectra Assure rl-scanner Docker image and also handles publishing the scan reports as pipeline artifacts in HTML, RL-JSON, CycloneDX, and SPDX formats.


**azure-pipelines_docker_ado_template.yml**

This pipeline script builds the .war file and scans it locally using a template called rl-secure-scan-ado.yml. The template leverages the Spectra Assure rl-scanner Docker image and also handles publishing the scan reports as pipeline artifacts in HTML, RL-JSON, CycloneDX, and SPDX formats.

## GitHub Action Examples

**rl-scan.yml**

A workflow, triggered manually only, that builds the .war file and scans it with the Spectra Assure CLI. The workflow installs the [rl-deploy](https://pypi.org/project/rl-deploy/) Python package, which is subsequently used to install and license the CLI. Scan reports in HTML, RL-JSON, CycloneDX, and SPDX formats are published as an artifact called "ReversingLabs reports".

**rl-scan-docker.yml**

This workflow does the same thing as above, but leverages the [Spectra Assure Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Scan reports in HTML, RL-JSON, CycloneDX, and SPDX formats are published as an artifact called "ReversingLabs reports".

**rl-scan-with-action.yml**

This workflow builds the .war file and scans it by leveraging the ReversingLabs GitHub Action "gh-action-rl-scanner-only". 

**rl-scan-with-composite-action.yml**

This workflow is configured to run only when a pull_request is opened or re-opened. It builds the .war file and scans it via the ReversingLabs GitHub Action "gh-action-rl-scanner-composite". After the scan completes, this action also runs the "github/codeql-action/upload-sarif" action to process the SARIF report generated by the scan.

**rl-scan-with-cloud-action-trial.yml**

This workflow builds the .war file and scans it by leveraging the published ReversingLabs GitHub Action "gh-action-rl-scanner-cloud-only". This action uses the [Spectra Assure Cloud Scan Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner-cloud). Here, the scan happens in the Spectra Assure cloud portal (the "Trial" instance to be exact). 

## TeamCity Examples

**.teamcity/settings.kts**

This is a TeamCity project settings file that defines two stages ("Build" and "RL scan") to build the .war file and scan it using the [Spectra Assure Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). Reports in HTML, RL-JSON, CycloneDX, and SPDX formats are saved in a zip file artifact. With each build, the HTML report is published under a tab called "ReversingLabs Report" within TeamCity. Documentation for integrating the Spectra Assure CLI with TeamCity can be found [here](https://docs.secure.software/cli/integrations/teamcity).

## GitLab Examples

**.gitlab-ci.yml**

This pipeline works in GitLab CI/CD runners. The pipeline has two stages - "build" and "test". The build stage has 1 job that builds the .war file. The test stage has 1 job that scans the .war using the [Spectra Assure Docker image](https://hub.docker.com/r/reversinglabs/rl-scanner). YAML provided by ReversingLabs ("rl-scanner-gitlab-include.yml") is used to handle kicking off the scan and saving the reports as job artifacts.
