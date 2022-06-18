def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      #mvn checkstyle:check
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
                        sh 'mvn clean compile'
                        env.ARGS = "-Dsonar.java.binaries=target/"
                        common.sonarcheck()
                    }
                }
            }
        }//End of stages
    }
}