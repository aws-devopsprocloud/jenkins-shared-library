def call (Map configMap){
    pipeline {
    agent {
        node {
            label 'AGENT-1'
        }
    } 
    options {
        disableConcurrentBuilds()
        ansiColor('xterm')
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        packageVersion = ''
        nexusURL = '172.31.12.76:8081'
    }
    parameters {
        booleanParam(name: 'DEPLOY', defaultValue: false, description: 'Toggle this value')
        choice(name: 'ENVIRONMENT', choices: ['dev', 'uat', 'prod'], description: 'Specify the target Environment?')
    }
    stages {
        stage('Getting the Package Version') {
            steps {
                script {
                    // def packageJSON = readJSON file: 'package.json'
                    def packageXML = readMavenPom file: 'pom.xml'
                    packageVersion = packageXML.version
                    echo "App Version is $packageVersion"
                }
            }
        }
        stage('Installing Maven') {
            steps {
                sh """
                   sudo  dnf install maven -y
                """
            }
        }
        stage('Installing Dependencies') {
            steps {
                sh """
                    mvn clean package 
                """
            }
        }
        stage('Renaming the Shipping-1.0') {
            steps {
                sh '''
                    pwd
                    echo "Current directory: $(pwd)"
                    cd /home/ec2-user/jenkins-agent/workspace/ipping-ci-multi-branch_feature-1/target
                    pwd
                    mv shipping-1.0.jar shipping.jar 
                '''
            }
        }
        stage('Unit Tests') {
            steps {
                echo "Unit tests will run here"
            }
        }
        stage('SonarQube Scanning') {
            steps {
                // sh """
                //     sonar-scanner
                // """
                echo "Code will be scanned by Sonar-Qube"
            }
        }
    //     stage('Building the Artifacts') {
    //         steps {
    //             sh """
    //                 ls -la
    //                 sudo dnf install zip -y
    //                 zip -q -r ${configMap.component}.jar ./* -x ".git" -x "*.zip"
    //                 ls -ltr
    //             """
    //         }
    //     }
    //     stage('Uploading the Artifacts to Nexus') {
    //         steps {
    //             nexusArtifactUploader(
    //                 nexusVersion: 'nexus3',
    //                 protocol: 'http',
    //                 nexusUrl: "${nexusURL}",
    //                 groupId: 'com.roboshop',
    //                 version: "${packageVersion}",
    //                 repository: "${configMap.component}",
    //                 credentialsId: 'nexus-auth',
    //                 artifacts: [
    //                     [artifactId: "${configMap.component}",
    //                     classifier: '',
    //                     file: "${configMap.component}.zip",
    //                     type: 'zip']
    //                 ]
    //             )
    //         }
    //     }
    //     stage('Giving the Package Version & Environment to CD') {
    //         when {
    //             expression {
    //                 params.DEPLOY
    //             }                  
    //         }
    //         steps {
    //             build job: "${configMap.component}-CD-${params.ENVIRONMENT}", 
    //             parameters: [
    //                 string(name: 'ENVIRONMENT', value: params.ENVIRONMENT),
    //                 string(name: 'VERSION', value: "${packageVersion}")
    //             ]
    //         }
    //     }
    // }
    }
    post {
        always {
            echo 'PIPELINE EXECUTION IS COMPLETED'
        }
        failure {
            echo 'The pipeline is FAILED'
        }
        success {
            echo 'The pipeline is SUCESS'
        }
    }
}
}