def lintChecks(){
    sh '''
      #~/node_modules/jslint/bin/jslint.js server.js
      echo lint checks
    '''
}