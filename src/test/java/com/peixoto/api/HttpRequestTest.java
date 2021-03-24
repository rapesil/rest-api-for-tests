package com.peixoto.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int localServerPort;

    @BeforeEach
    public void setup() {
        baseURI = "http://localhost";
        port = localServerPort;
    }

    @Test
    @DisplayName("When receive a get request in root path, should return Hello, World.")
    void shouldReturnHelloWorld() {
        given()
        .when()
            .get("/")
        .then()
            .statusCode(200)
            .body("hello", Matchers.is("Hello, World"));
    }

}
