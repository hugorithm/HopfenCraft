FROM openjdk:21-slim

COPY target/*.jar app.jar

COPY uploads /uploads/ 

ENTRYPOINT ["java", "-jar", "/app.jar"]
