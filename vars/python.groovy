def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      #mvn checkstyle:check
      pylint *.py
      #echo lint checks for ${COMPONENT}
    '''
}

def call(){
    pipeline{
        agent any

        stages{

            //lint checks
            stage('Lint Checks'){
                steps{
                    script{
                        lintChecks()
                    }
                }
            }
        }//End of stages
    }
}