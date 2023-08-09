FROM openjdk:17-jdk-slim

WORKDIR /bitespeed

# Copy the application JAR file to the container
COPY target/backend-task*.jar backend-task.jar

# Set the entry point command to run the application
CMD ["java", "-jar", "backend-task.jar"]
