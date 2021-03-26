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

The command above will start a local server in `localhost:8080`. Now, you can point your test to this server.

## Available endpoints

For while only one endpoint is available

> UPDATE: Now you should authenticate before send your request

* user: user
* pass: 123456

`GET` / - should return status code 200 and a simple JSON

```json
{
    "hello": "Hello, World"
}
```