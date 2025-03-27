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
                    withEnv(["JAVA_HOME=${env.JAVA_HOME}", "PATH+MAVEN=${env.MAVEN_HOME}/bin:${env.PATH}"]) {
                        sh 'mvn clean package -pl publisher -am'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                script {
                    withEnv(["JAVA_HOME=${env.JAVA_HOME}", "PATH+MAVEN=${env.MAVEN_HOME}/bin:${env.PATH}"]) {
                        sh 'mvn test -pl publisher'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                    def imageName = "publisher:latest"
                    sh "docker build -t ${imageName} -f publisher/Dockerfile ."
                }
            }
        }

        stage('Publish Docker Image') {
            steps {
                echo 'Publishing Docker image...'
                script {
                    def imageName = "publisher:latest"
                    docker.withRegistry('https://your-docker-registry', 'docker-credentials-id') {
                        sh "docker push ${imageName}"
                    }
                }
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        }
    }
}