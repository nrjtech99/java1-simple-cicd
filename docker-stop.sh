docker ps -q --filter ancestor=jenkins/jenkins:2.414.3-jdk17 | xargs docker stop
