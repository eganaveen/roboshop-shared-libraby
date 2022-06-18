def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      echo lint checks for ${COMPONENT}
    '''
}

def sonarcheck(){
    sh '''
      sonar-scanner -Dsonar.host.url=http://172.31.26.97:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
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
                        sonarcheck()
                    }
                }
            }

        }//End of stages
    }
}