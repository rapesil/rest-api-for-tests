#sintax-docker/dockerfile:1

# It build application to generate jar
FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


# It gets a jar generated and prepare image to run
FROM openjdk:11
COPY build/libs/*.jar /app.jar
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
ENV SPRING_DATASOURCE_URL jdbc:mysql://db:3306/book?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true