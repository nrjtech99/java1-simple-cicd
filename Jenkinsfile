pipeline {
    agent any

    environment {
        MAVEN_HOME = tool name: 'Maven 3.8.4', type: 'maven'
        JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
    }

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

                        sh 'mvn clean package'

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
    }

    post {
        always {
            echo 'Cleaning up...'

        }
    }
}