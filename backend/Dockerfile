FROM gradle:jdk21-alpine AS build

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM openjdk:21-slim

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

USER appuser

WORKDIR /app

COPY --from=build app/build/libs/*.jar app.jar

COPY uploads /uploads/ 

ENTRYPOINT ["java", "-jar", "app.jar"]
