pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building AJ project..'
                bat 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing AJ project..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying AJ project....'
            }
        }
    }
}