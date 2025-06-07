pipeline {
    agent any

    environment {
        BRANCH_NAME = env.BRANCH_NAME
    }

    parameters {
        choice(name: 'RELEASE_SCOPE', choices: ['parent-common', 'poller', 'processor', 'all'], description: 'Define the scope for release branch operations. "all" will release parent-common first, then poller, then processor.')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checking out code for branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        // --- Develop Branch Stages ---
        stage('Build (develop)') {
            when { branch 'develop' }
            steps {
                echo 'Building the project for develop branch...'
                bat 'mvn clean install -B'
            }
        }

        stage('Test (develop)') {
            when { branch 'develop' }
            steps {
                echo 'Running tests for develop branch...'
                bat 'mvn test -B'
            }
        }

        stage('Build Docker Images (develop)') {
            when { branch 'develop' }
            steps {
                script {
                    echo 'Building Docker images for develop branch...'
                    bat 'mvn package -DskipTests -B -pl poller,processor -am'
                }
            }
        }

        stage('Publish Docker Images (develop)') {
            when { branch 'develop' }
            environment {
                DOCKER_HUB_CREDENTIALS_ID = 'your-dockerhub-credentials-id'
            }
            steps {
                script {
                    echo 'Publishing Docker images for develop branch...'
                    withCredentials([usernamePassword(credentialsId: env.DOCKER_HUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat "mvn deploy -DskipTests -B -pl poller,processor -am -Ddockerfile.username=${DOCKER_USER} -Ddockerfile.password=${DOCKER_PASS}"
                    }
                }
            }
        }

        // --- Release Branch Stages ---
        stage('Prepare Release (release)') {
            when {
                branch pattern: 'release/.*', comparator: 'REGEXP'
            }
            steps {
                script {
                    echo "Preparing release for scope: ${params.RELEASE_SCOPE} on branch ${env.BRANCH_NAME}"
                    env.MVN_RELEASE_ARGS = "-B -Dresume=false -Darguments=\"-DskipTests -DaltDeploymentRepository=local-repo::default::file:./target/repo\" "

                    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-id-rsa', keyFileVariable: 'SSH_KEY_PATH')]) {
                        bat 'git config user.name "Jenkins CI"'
                        bat 'git config user.email "jenkins@example.com"'

                        if (params.RELEASE_SCOPE == 'parent-common' || params.RELEASE_SCOPE == 'all') {
                            echo 'Preparing release for parent and common modules...'
                            bat "mvn release:prepare ${env.MVN_RELEASE_ARGS} -DautoVersionSubmodules=false"
                        }
                        if (params.RELEASE_SCOPE == 'poller' || params.RELEASE_SCOPE == 'all') {
                            echo 'Preparing release for poller module...'
                            bat "mvn release:prepare ${env.MVN_RELEASE_ARGS} -f poller/pom.xml"
                        }
                        if (params.RELEASE_SCOPE == 'processor' || params.RELEASE_SCOPE == 'all') {
                            echo 'Preparing release for processor module...'
                            bat "mvn release:prepare ${env.MVN_RELEASE_ARGS} -f processor/pom.xml"
                        }
                    }
                }
            }
        }

        stage('Perform Release (release)') {
            when {
                branch pattern: 'release/.*', comparator: 'REGEXP'
            }
            steps {
                script {
                    echo "Performing release for scope: ${params.RELEASE_SCOPE} on branch ${env.BRANCH_NAME}"
                    env.MVN_RELEASE_ARGS = "-B -Dresume=false -Darguments=\"-DskipTests -DaltDeploymentRepository=local-repo::default::file:./target/repo\" "

                    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-id-rsa', keyFileVariable: 'SSH_KEY_PATH')]) {
                        if (params.RELEASE_SCOPE == 'parent-common' || params.RELEASE_SCOPE == 'all') {
                            echo 'Performing release for parent and common modules...'
                            bat "mvn release:perform ${env.MVN_RELEASE_ARGS} -DautoVersionSubmodules=false"
                        }
                        if (params.RELEASE_SCOPE == 'poller' || params.RELEASE_SCOPE == 'all') {
                            echo 'Performing release for poller module...'
                            bat "mvn release:perform ${env.MVN_RELEASE_ARGS} -f poller/pom.xml"
                        }
                        if (params.RELEASE_SCOPE == 'processor' || params.RELEASE_SCOPE == 'all') {
                            echo 'Performing release for processor module...'
                            bat "mvn release:perform ${env.MVN_RELEASE_ARGS} -f processor/pom.xml"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Pipeline successful.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
