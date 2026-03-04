import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection

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

version = "2025.11"

project {

    buildType(Build)

    features {
        awsConnection {
            id = "FreeDiskSpace_AmazonWebServicesAws"
            name = "Amazon Web Services (AWS)"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVJFGDHSDZ"
                secretAccessKey = "credentialsJSON:5a9d6a89-08b4-45a0-84fd-312575389ff4"
            }
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_58"
            profileId = "amazon-5"
            agentPoolId = "-2"
            name = "123"
            vpcSubnetId = "subnet-0ace2a91ee63119ea,subnet-043178c302cabfe37"
            instanceType = "t2.micro"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0e303c7d2285132c7")
        }
        amazonEC2CloudProfile {
            id = "amazon-5"
            name = "123"
            terminateIdleMinutes = 90
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "FreeDiskSpace_AmazonWebServicesAws"
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "ls"
        }
    }

    features {
        perfmon {
        }
        freeDiskSpace {
            requiredSpace = "4096 MB"
            failBuild = true
        }
    }
})
