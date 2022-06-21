
def call() {
    node {
        git branch: "main", url: "https://github.com/eganaveen/${COMPONENT}"
        env.APP_TYPE = "python"
        common.lintChecks()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null){
            common.artifacts()
        }
    }
}
//
//
//
//
//
//env.APP_TYPE="python"
//
//def call(){
//    pipeline{
//        agent any
//
//        environment{
//            SONAR = credentials('SONAR')
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
//            //Sonar qube quality check
//            stage('sonarqube'){
//                steps{
//                    script{
//                        env.ARGS = "-Dsonar.sources=."
//                        common.sonarcheck()
//                    }
//                }
//            }
//
//            //Test cases
//            stage('TestCases'){
//                parallel{
//
//                    stage('Unit Test'){
//                        steps{
//                            sh 'echo unit test'
//                        }
//                    }
//                    stage('Integration Test'){
//                        steps{
//                            sh 'echo Integration test'
//                        }
//                    }
//                    stage('Functional Test'){
//                        steps{
//                            sh 'echo Functional test'
//                        }
//                    }
//
//                }
//            }
//        }//End of stages
//    }
//}