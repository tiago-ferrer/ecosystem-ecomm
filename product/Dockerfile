# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the project files
COPY target/product-0.0.1-SNAPSHOT.jar product.jar

# Expose the port the application runs on
EXPOSE 8083

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl --fail http://localhost:8083/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "product.jar"]