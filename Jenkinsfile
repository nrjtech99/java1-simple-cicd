pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code...'
                bat 'git checkout master'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                script {

                        bat 'mvn clean install'

                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'

            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'

            }
        }

        stage('Publish Docker Image') {
            steps {
                echo 'Publishing Docker image...'

            }
        }

        stage('Release') {
            steps {
                echo 'Releasing...'
                 withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-java1-simple-cicd-key', keyFileVariable: 'SSH_KEY')]) {
                            bat 'git tag'
                            bat 'git tag -d test-tag'
                            bat 'git tag test-tag'
                            bat 'git push origin test-tag'
                            bat 'git tag -d test-tag'
                            bat 'git pull --tags'
                            bat 'git pull'

                            bat 'mvn release:prepare release:perform'
                 }
            }
        }


    }

    post {
        always {
            echo 'Cleaning up...'

        }
    }
}