def call() {
    parameters {
        choice(choices: ['dev', 'prod'], description: "Pik ENV", name: "ENV")
    }
    node {
        git branch: "main", url: "https://github.com/eganaveen/${REPONAME}"
        stage('Terrafile init'){
            sh 'terrafile -f ${ENV}-env/Terrafile'
        }
        stage('Terraform init'){
            sh 'terraform init -backend-config=${ENV}-env/${ENV}-backend.tfvars'
        }
        stage('Terraform plan'){
            sh 'terraform plan -var-file=${ENV}-env/${ENV}.tfvars'
        }
        stage('Terraform plan'){
            sh 'terraform apply -var-file=${ENV}-env/${ENV}.tfvars --auto-approve'
        }
    }
}