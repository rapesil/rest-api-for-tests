package com.peixoto.api.controllers;

import com.peixoto.api.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;

@Log4j2
public class UsersTests extends BaseIntegrationTest {

    private static final String VALID_USER_ADMIN = "admin1";
    private static final String VALID_PASS_ADMIN = "test";

    @BeforeEach
    public void setup() {
        baseURI = "http://localhost";
        basePath = "";
        port = getPort();
    }

    @Test
    void get_shouldListAllUser() {
        given()
            .auth().basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
        .when()
            .get("/users")
        .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.SC_OK);
    }

}
