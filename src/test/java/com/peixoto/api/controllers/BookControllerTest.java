package com.peixoto.api.controllers;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @LocalServerPort
    private int localPort;

    private static final String VALID_USER_ADMIN = "admin1";
    private static final String VALID_PASS_ADMIN = "test";
    private static final String INVALID_PASS_ADMIN = "invalidPass";

    @BeforeEach
    public void setup() {
        baseURI = "http://localhost";
        basePath = "books";
        port = localPort;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    void getAll_shouldListAllBooks_whenHavePermission(String user, String pass) throws Exception {
        given()
            .log().all()
            .auth()
            .basic(user, pass)
        .when()
            .get("/")
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void getAll_shouldReturnNotAuthorized() throws Exception {
        when()
            .get("/")
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void getAll_shouldReturnForbidden() throws Exception {
        given()
            .auth().basic(VALID_USER_ADMIN, INVALID_PASS_ADMIN)
        .when()
            .get("/")
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }



}
