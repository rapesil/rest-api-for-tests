[![Java CI with Gradle](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml)
# REST-API-FOR-TESTS

`REST-API-FOR-TEST` is a simple project that I've created to improve my skills as a developer. My only intention with this is to train my abilities. I am developing a simple REST API to use as examples in my medium articles and train my skills as a tester.

I'm using `SpringBoot` to build my API and `SpringBootTest` with `Rest Assured` and `JUnit 5` to create my tests. 

> This project is in continuous progress. I wish to create more good examples of how to do unit and integration tests soon.

## How to run

You can clone this repository and run all test locally just typing it:

```bash
./gradlew test
```  
  
If you prefer to test this API against `POSTMAN`, you should run:

```bash
./gradlew bootRun
```

The command above will start a local server in `localhost:8089`. Now, you can point your test to this server.

## Progress

You can request to `/books` endpoint using methods `GET`, `POST`, `PUT` and `DELETE`.

I've created `/books/external` just to simulate an external call to another api. This endpoint just call `https://reqres.in/api/users?page=2`
. 

## Authentication and Authorization

Some functionalities needs you have a valid user. Actually we have two users:

* ADMIN with password `123456`. 
* USER with password `123`