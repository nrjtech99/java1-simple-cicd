pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code...'
                checkout scm
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
                bat 'mvn release:prepare release:perform'
            }
        }


    }

    post {
        always {
            echo 'Cleaning up...'

        }
    }
}