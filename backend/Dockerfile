FROM openjdk:21-slim

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

USER appuser

WORKDIR /app

COPY target/*.jar app.jar

COPY uploads /uploads/ 

ENTRYPOINT ["java", "-jar", "app.jar"]
