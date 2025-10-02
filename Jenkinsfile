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
                // Apply the custom policy file from SCM
                sh 'cp -v rl-policy/policy.info /bin/RLSecure/.rl-secure/projects/Apache/packages/struts2-showcase/.package-policy.info'
                
                // Scan
                sh '/bin/RLSecure/rl-secure scan target/struts2-showcase.war -s /bin/RLSecure Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --no-tracking'
                
                // Generate reports, including the RL-SAFE archive
                sh '/bin/RLSecure/rl-secure report -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --format cyclonedx,spdx,rl-html,rl-json --output-path RLreports/$BUILD_NUMBER'
                sh '/bin/RLSecure/rl-safe pack -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --format all --output-path RLreports/$BUILD_NUMBER --no-tracking'
                
                // Output summary-level results
                sh '/bin/RLSecure/rl-secure list -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --show-all --no-color'

                // Return the status of the scan. 0=pass, 1=fail
                sh '/bin/RLSecure/rl-secure status --return-status -s /bin/RLSecure -p Apache/struts2-showcase@2.5.28_$BUILD_NUMBER --no-color'
            }
        }
    }
    post {
        always {
            // Move reports to latest and archive them
            sh 'mkdir -p RLreports/latest && cp -v RLreports/$BUILD_NUMBER/*.json RLreports/latest && cp -v RLreports/$BUILD_NUMBER/*.rl-safe RLreports/latest'
            archiveArtifacts artifacts: 'RLreports/latest/*.json', onlyIfSuccessful: false
            archiveArtifacts artifacts: 'RLreports/latest/*.rl-safe', onlyIfSuccessful: false
            publishHTML([reportDir: 'RLreports/$BUILD_NUMBER/rl-html', reportFiles: 'sdlc.html', reportName: 'ReversingLabs Report', allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportTitles: '', useWrapperFileDirectly: true])
        }
    }    
}
