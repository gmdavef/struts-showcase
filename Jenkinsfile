pipeline {
    agent any
    tools {
        maven 'mvn3.9.1'
    }    
    stages {
        stage('Build') {
            steps {
                withMaven {
                    sh 'mvn clean package'
                }
            }
        } 
        stage('RLTest') {
            steps {
                sh '/bin/RLSecure/rl-secure scan target/struts2-showcase.war -s /bin/RLSecure WebApps/struts2-showcase@v2.5.28_$BUILD_NUMBER --keep-reference'                
                sh '/bin/RLSecure/rl-secure status -s /bin/RLSecure -p WebApps/struts2-showcase@v2.5.28_$BUILD_NUMBER --no-color'
                sh '/bin/RLSecure/rl-secure report -s /bin/RLSecure -p WebApps/struts2-showcase@v2.5.28_$BUILD_NUMBER --format cyclonedx,rl-html,rl-json --output-path RLreports/$BUILD_NUMBER'
                sh 'mkdir -p RLreports/latest && cp RLreports/$BUILD_NUMBER/*.json RLreports/latest'
            }
        }
    }
    post {
        always {
            publishHTML([reportDir: 'RLreports/$BUILD_NUMBER/rl-html', reportFiles: 'sdlc.html', reportName: 'ReversingLabs Report', allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportTitles: '', useWrapperFileDirectly: true])
            archiveArtifacts artifacts: 'RLreports/latest/*.json', onlyIfSuccessful: true
        }
    }    
}
