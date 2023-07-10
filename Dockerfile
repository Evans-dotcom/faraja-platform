FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/faraja-platform-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java","-jar","faraja-platform-0.0.1-SNAPSHOT.jar"]
EXPOSE 9090