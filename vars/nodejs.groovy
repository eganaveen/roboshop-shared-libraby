def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      echo lint checks for ${COMPONENT}
    '''
}



def call(){
    pipeline{
        agent any

        environment{
            SONAR = credentials('SONAR')
            NEXUS = credentials('NEXUS')
        }
        stages{

            //lint checks
            stage('Lint Checks'){
                steps{
                    script{
                        lintChecks()
                    }
                }
            }

            //Sonar qube quality check
            stage('sonarqube'){
                steps{
                    script{
                        env.ARGS = "-Dsonar.sources=."
                        common.sonarcheck()
                    }
                }
            }

            //Test cases
            stage('TestCases'){
                parallel{

                    stage('Unit Test'){
                        steps{
                            sh 'echo unit test'
                        }
                    }
                    stage('Integration Test'){
                        steps{
                            sh 'echo Integration test'
                        }
                    }
                    stage('Functional Test'){
                        steps{
                            sh 'echo Functional test'
                        }
                    }

                }

            }

            stage('Prepare Artifact'){
                when{
                    expression { env.TAG_NAME != null }
                }
                steps{
                    sh '''
                        npm install
                        zip -r {COMPONENT}-${TAG_NAME}.zip node_modules server.js
                    '''
                }
            }
            stage('Upload artifact to nexus'){
                when{
                    expression { env.TAG_NAME != null }
                }
                steps{
                    sh'''
                        curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.21.99:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                    '''
                }
            }
        }//End of stages
    }
}