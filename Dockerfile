#sintax-docker/dockerfile:1
FROM gradle:7.2.0-jdk11
MAINTAINER rapesil
EXPOSE 8080
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN ./gradlew build -x test -x integrationTest --no-daemon
COPY /jar/rest-service-0.1.jar /rest-service-0.1.jar
ENTRYPOINT ["java","-jar","/rest-service-0.1.jar"]
ENV SPRING_DATASOURCE_URL jdbc:mysql://db:3306/book?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true


