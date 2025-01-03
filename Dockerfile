FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
ADD /src /src
ADD pom.xml pom.xml
RUN mvn clean package

FROM openjdk:21
COPY --from=build /target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]