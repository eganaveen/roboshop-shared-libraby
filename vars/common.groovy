def lintChecks(){
    stage{
        if (APP_TYPE == "nodejs"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              echo lint checks for ${COMPONENT}
            '''
        }
        else if (APP_TYPE == "maven"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              #mvn checkstyle:check
              echo lint checks for ${COMPONENT}
            '''
        }
        else if (APP-TYPE == "python"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              #mvn checkstyle:check
              #pylint *.py
              echo lint checks for ${COMPONENT}
            '''
        }
        else (APP-TYPE == "golang"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              echo lint checks for ${COMPONENT}
            '''
            }
    }
}
def sonarCheck(){
    stage{
        sh '''
          #sonar-scanner -Dsonar.host.url=http://172.31.26.97:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
          #sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.26.97 ${COMPONENT}
          echo sonar checks for ${COMPONENT}
        '''
    }
}
def testCases(){
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
}