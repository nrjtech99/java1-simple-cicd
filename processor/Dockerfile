ARG JAR_FILE=target/processor.jar
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
