def call() {
    node {
        sh 'rm -rf *'
        git branch: "main", url: "https://github.com/eganaveen/${COMPONENT}"
        env.app_type="front"
        common.lintChecks()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if (env.TAG_NAME != null){
            common.artifacts()
        }
    }
}