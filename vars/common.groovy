def lintChecks(){
    stage('Lint Checks'){
        if (env.APP_TYPE == "nodejs"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              echo lint checks for ${COMPONENT}
            '''
        }
        else if (env.APP_TYPE == "maven"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              #mvn checkstyle:check
              echo lint checks for ${COMPONENT}
            '''
        }
        else if (env.APP_TYPE == "python"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              #mvn checkstyle:check
              #pylint *.py
              echo lint checks for ${COMPONENT}
            '''
        }
        else if (env.APP_TYPE == "golang"){
            sh '''
              #~/node_modules/jslint/bin/jslint.js server.js
              echo lint checks for ${COMPONENT}
            '''
        }
    }
}
def sonarCheck(){
    stage('Sonar Analysis'){
        sh '''
          #sonar-scanner -Dsonar.host.url=http://172.31.26.97:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
          #sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.26.97 ${COMPONENT}
          echo sonar checks for ${COMPONENT}
        '''
    }
}
def testCases(){
    stage('TestCases'){

        def stages = [:]

        stages["Unit Test"] = {
            sh "echo unit test"
        }
        stages["Integration Test"] = {
            sh "echo Integration test"
        }
        stages["Functional Test"] = {
            sh "echo Functional test"
        }

        parallel(stages)
    }
}
def artifacts(){
    stage('Check the release'){
        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -s -L http://172.31.1.142:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print UPLOAD_STATUS
    }
    if (env.UPLOAD_STATUS == ""){
        stage('Prepare Artifact'){
            if (APP_TYPE == "nodejs") {
                sh '''
                    npm install
                    zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                '''
            }
            else if (APP_TYPE == "maven") {
                sh '''
                    echo
                '''
            }
            else if (APP_TYPE == "python") {
                sh '''
                    echo
                '''
            }
            else if (APP_TYPE == "golang") {
                sh '''
                    echo
                '''
            }
        }
        stage('Upload artifact to nexus'){
            withCredentials([usernamePassword(credentialsId:'NEXUS', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]){
                sh'''
                    curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.1.142:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                '''
            }
        }
    }
}
