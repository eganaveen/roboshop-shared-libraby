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
                stage('Prepare Artifact'){
                    when{
                        expression { env.TAG_NAME != null }
                    }
                    steps{
                        sh 'echo prepare'
                    }
                }
                stage('Upload Artifact'){
                    when{
                        expression { env.TAG_NAME != null }
                    }
                    steps{
                        sh 'echo upload'
                    }
                }
            }

            stage('Prepare Artifact'){
                when{
                    expression { env.TAG_NAME != null }
                }
                steps{
                    sh 'echo prepare artifact'
                }
            }
            stage('Upload artifact to nexus'){
                when{
                    expression { env.TAG_NAME != null }
                }
                steps{
                    sh 'echo upload artifact to nexus'
                }
            }
        }//End of stages
    }
}