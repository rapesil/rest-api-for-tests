package com.peixoto.api;

import org.apache.http.HttpStatus;
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
    @DisplayName("When receive a get request in root path with admin, should return Hello, World.")
    void shouldReturnHelloWorld() {
        given()
            .auth()
                .basic("admin", "123456")
        .when()
            .get("/")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body(Matchers.is("API is up and running"));
    }

    @Test
    @DisplayName("When receive a get request in root path with simple user, should return Forbidden")
    void shouldBlockAccessToNonAdminUsers() {
        given()
            .auth()
            .basic("user", "123")
        .when()
            .get("/")
        .then()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("When receive a get request in root path without authenticated user, should return Unauthorized")
    void shouldBlockNonAuthenticatedUser() {
        given()
            .auth().none()
        .when()
            .get("/")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

}
