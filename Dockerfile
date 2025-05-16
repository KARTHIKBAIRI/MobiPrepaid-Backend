# Step 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the built jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/mobile-recharge-0.0.1-SNAPSHOT.jar app.jar
COPY tidb-ca.pem /app/tidb-ca.pem
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
