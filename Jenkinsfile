pipeline {
    agent {
        dockerfile true
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building AJ project..'
                sh 'echo myEnv=$myEnv and myEnv2=$myEnv2'
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