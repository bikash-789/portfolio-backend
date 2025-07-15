FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Set production profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run application
CMD ["java", "-jar", "target/portfolio-backend-0.0.1-SNAPSHOT.jar"] 