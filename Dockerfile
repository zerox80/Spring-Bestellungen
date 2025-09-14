# Multi-stage Dockerfile for Spring Boot (Java 21)

# 1) Build stage: use Maven with Temurin JDK 21
FROM maven:3.9.8-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy only pom.xml first to leverage dependency caching
COPY pom.xml .
# Pre-fetch dependencies (standard RUN to be compatible without BuildKit)
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy the rest of the sources and build
COPY src ./src
RUN mvn -B -Dmaven.test.skip=true clean package

# 2) Runtime stage: slim JRE 21 image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/bestellsystem-1.0.0-SNAPSHOT.jar app.jar

# Optional JVM options (e.g. -Xms256m -Xmx512m)
ENV JAVA_OPTS=""

EXPOSE 8080

# Use sh -c to allow JAVA_OPTS injection
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
