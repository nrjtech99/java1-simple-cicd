pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code...'
                bat 'git checkout master'
                bat 'git pull'
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
                 withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-id-rsa', keyFileVariable: 'SSH_KEY')]) {
                            bat 'git tag'

                            bat 'git pull --tags'
                            bat 'git pull'

                            bat 'mvn release:prepare -DautoVersionSubmodules=false'
                            bat 'mvn release:perform'
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