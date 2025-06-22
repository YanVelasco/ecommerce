# Dockerfile for Accounts Service
FROM openjdk:21-jdk-slim

MAINTAINER yanvelasco

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/ecommerce-0.0.1-SNAPSHOT.jar /app/ecommerce-0.0.1-SNAPSHOT.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "ecommerce-0.0.1-SNAPSHOT.jar"]