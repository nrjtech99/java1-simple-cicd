# Use the Maven image with Amazon Corretto 17 for the build stage
FROM maven:3.8.4-amazoncorretto-17 AS build

# Set the working directory
WORKDIR /app

# Copy the parent pom.xml and the module pom.xml files
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY publisher/pom.xml publisher/pom.xml
COPY subscriber/pom.xml subscriber/pom.xml

# Copy the source code of the modules
COPY common/src ./common/src
COPY publisher/src ./publisher/src
COPY subscriber/src ./subscriber/src

# Build and install the common module
RUN mvn clean install -pl common -am

# Download dependencies for the publisher module
RUN mvn dependency:go-offline -pl subscriber -am

# Compile and package the publisher module
RUN mvn clean package -pl subscriber -am

# Use the Amazon Corretto 17 image for the runtime stage
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/subscriber/target/*.jar app.jar

# Specify the command to run the application
CMD ["java", "-jar", "app.jar"]