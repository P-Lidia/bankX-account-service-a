FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/account-service-a-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]