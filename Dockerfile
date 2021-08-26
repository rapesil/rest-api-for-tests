FROM openjdk:11
EXPOSE 8080
MAINTAINER rapesil
ENV SPRING_DATASOURCE_URL jdbc:mysql://db:3306/book?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
COPY build/libs/rest-service-0.1.jar rest-service-0.1.jar
ENTRYPOINT ["java","-jar","/rest-service-0.1.jar"]
