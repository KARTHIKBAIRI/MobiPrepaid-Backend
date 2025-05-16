FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/mobile-recharge-0.0.1-SNAPSHOT.jar app.jar
COPY tidb-ca.pem /app/tidb-ca.pem
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]