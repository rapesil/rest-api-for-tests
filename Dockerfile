FROM openjdk:11

MAINTAINER peixoto

ADD build/distributions/*.zip /rest-api-for-tests.zip
RUN unzip rest-api-for-tests.zip && \
#    rm -rf *.zip && \
    mv rest-api-for-tests-* rest-api-for-tests

EXPOSE 80 443

ENTRYPOINT ["/rest-api-for-tests/bin/rest-api-for-tests"]
ENV SPRING_DATASOURCE_URL jdbc:mysql://db:3306/book?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
