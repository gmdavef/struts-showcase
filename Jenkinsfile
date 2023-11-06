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
                // Load the custom policy from SCM
                sh 'cp rl-policies/struts-showcase-policy.info /bin/RLSecure/.rl-secure/projects/Apache/packages/struts2-showcase/.package-policy.info'
                
                // Scan
                sh '/bin/RLSecure/rl-secure scan target/struts2-showcase.war -s /bin/RLSecure Apache/struts2-showcase@2.5.28_$BUILD_NUMBER'
                
                // Generate reports
                sh '/bin/RLSecure/rl-secure report -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --format cyclonedx,spdx,rl-html,rl-json --output-path RLreports/$BUILD_NUMBER'
                
                // The following line will return a non-zero exit code (and cause step to fail) if any P0 issues are detected by the scan.
                sh '/bin/RLSecure/rl-secure status -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --no-color --return-status'
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
