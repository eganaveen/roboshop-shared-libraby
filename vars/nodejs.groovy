def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      echo lint checks
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
                        nodejs.lintChecks()
                    }
                }
            }
        }//End of stages
    }
}