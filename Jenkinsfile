pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven( maven: 'mvn3' ) {
                    sh 'mvn clean package'
                }
            }
        } 
        stage('RLTest') {
            steps {
                sh '/bin/RLSecure/rl-secure scan target/struts2-showcase.war -s /bin/RLSecure Apache/struts2-showcase@2.5.28_$BUILD_NUMBER'
                sh '/bin/RLSecure/rl-secure report -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --format cyclonedx,spdx,rl-html,rl-json --output-path RLreports/$BUILD_NUMBER'
                // sh '/bin/RLSecure/rl-secure status -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --no-color --return-status'
            }
        }
    }
    post {
        always {
            sh 'mkdir -p RLreports/latest && cp RLreports/$BUILD_NUMBER/*.json RLreports/latest'
            archiveArtifacts artifacts: 'RLreports/latest/*.json', onlyIfSuccessful: false
            publishHTML([reportDir: 'RLreports/$BUILD_NUMBER/rl-html', reportFiles: 'sdlc.html', reportName: 'ReversingLabs Report', allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportTitles: '', useWrapperFileDirectly: true])
        }
    }    
}
