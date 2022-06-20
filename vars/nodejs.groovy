env.APP_TYPE = "nodejs"
def call() {
    node {
        common.lintChecks()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
    }
}

//    pipeline{
//        agent any
//
//        environment{
//            SONAR = credentials('SONAR')
//            NEXUS = credentials('NEXUS')
//        }
//        stages{
//
//            //lint checks
//            stage('Lint Checks'){
//                steps{
//                    script{
//                        lintChecks()
//                    }
//                }
//            }
//
//            //Sonar qube quality check
//            stage('sonarqube'){
//                steps{
//                    script{
//                        env.ARGS = "-Dsonar.sources=."
//                        common.sonarCheck()
//                    }
//                }
//            }

//            //Test cases
//
//            //check the release
//            stage('Check the release'){
//                when{
//                    expression { env.TAG_NAME != null }
//                }
//                steps {
//                    script{
//                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -s -L http://54.92.218.237:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                    }
//                }
//            }
//            stage('Prepare Artifact'){
//                when{
//                    expression { env.TAG_NAME != null }
//                    expression { env.UPLOAD_STATUS == "" }
//                }
//                steps{
//                    sh '''
//                        npm install
//                        zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
//                    '''
//                }
//            }
//            stage('Upload artifact to nexus'){
//                when{
//                    expression { env.TAG_NAME != null }
//                    expression { env.UPLOAD_STATUS == "" }
//                }
//                steps{
//                    sh'''
//                        curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.21.99:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
//                    '''
//                }
//            }
//        }//End of stages
//    }
//}