docker run -p 8080:8080 -p 50000:50000 --restart=on-failure --dns 1.1.1.1 --dns 8.8.8.8 jenkins/jenkins:2.414.3-jdk17
