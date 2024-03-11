import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.ExecBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {

    buildType(RlScan)
    buildType(Build)

    features {
        buildReportTab {
            id = "PROJECT_EXT_2"
            title = "ReversingLabs Report"
            startPage = "rl-secure-report.zip!rl-html/sdlc.html"
        }
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "target/struts2-showcase.war"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean package"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }

    features {
        perfmon {
        }
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:757df03b-8d63-4164-b149-87c2c91a8787"
                }
            }
        }
    }
})

object RlScan : BuildType({
    name = "RL scan"

    artifactRules = "%system.teamcity.build.tempDir%/reports/** => rl-secure-report.zip"
    maxRunningBuilds = 5

    params {
        password("env.RLSECURE_SITE_KEY", "credentialsJSON:42b13434-d914-4748-97a8-361ec1afc6d3", label = "RL site key", display = ParameterDisplay.HIDDEN)
        password("env.RLSECURE_ENCODED_LICENSE", "credentialsJSON:0fb03933-3181-4718-af4c-47396374b230", label = "RL encoded license", display = ParameterDisplay.HIDDEN)
    }

    steps {
        exec {
            name = "Scan package"
            path = "rl-scan"
            arguments = "--package-path=%system.teamcity.build.tempDir%/packages/struts2-showcase.war --report-path=%system.teamcity.build.tempDir%/reports --message-reporter=teamcity"
            dockerImage = "reversinglabs/rl-scanner"
            dockerImagePlatform = ExecBuildStep.ImagePlatform.Linux
            dockerPull = true
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${Build.id}"
            successfulOnly = true
        }
    }

    dependencies {
        artifacts(Build) {
            buildRule = lastSuccessful()
            artifactRules = "struts2-showcase.war => %system.teamcity.build.tempDir%/packages"
        }
    }
})
