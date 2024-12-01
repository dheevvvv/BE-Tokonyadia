FROM openjdk:17-alpine
WORKDIR /app
COPY target/tokonyadia-api-0.0.1-SNAPSHOT.jar tokonyadia-api.jar

ENTRYPOINT ["java", "-jar", "tokonyadia-api.jar"]