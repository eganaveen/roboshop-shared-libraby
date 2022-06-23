def call() {
    node {
        sh 'rm -rf *'
        git branch: "main", url: "https://github.com/eganaveen/${COMPONENT}"
        sh 'ls -l'
        env.app_type="front"
        sh 'echo this is from frontend'
        common.lintChecks()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null){
            common.artifacts()
        }
    }
}