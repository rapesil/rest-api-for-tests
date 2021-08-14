package com.peixoto.api.controllers;

import com.peixoto.api.services.BookService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.when;

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private BookService bookService;

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
            .get("/" )
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
