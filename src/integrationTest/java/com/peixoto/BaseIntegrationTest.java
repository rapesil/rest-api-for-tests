package com.peixoto;

import com.peixoto.api.TestingWebApplication;
import com.peixoto.api.repository.BookRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Tag("integrationTest")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes =  TestingWebApplication.class
)
@ActiveProfiles("test")
@Sql(
    value = {"/schema.sql", "/data.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class BaseIntegrationTest {

    @LocalServerPort
    private int localPort;

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    @Test
    @Disabled
    void apiTest() {
        RestAssured.given()
             .auth()
             .basic("user1", "test")
             .when()
             .get("/books")
             .then()
             .statusCode(200)
             .body("size()", Matchers.is(10));
    }

    @Test
    void deveBuscarTodosOsLivros() {
        RestAssured.given()
            .auth()
            .basic("user1", "test")
            .when()
            .get("/books")
            .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }

    @Test
    void deveCadastrarUmNovoLivro() {
        RestAssured.given()
            .auth()
            .basic("admin1", "test")
            .contentType(ContentType.JSON)
            .body("{\n" +
                      "    \"category\": \"q\",\n" +
                      "    \"author\": \"Rafael Peixoto\",\n" +
                      "    \"title\": \"12345\"\n" +
                      "}")
            .when()
            .post("/books")
            .then()
            .statusCode(201);

        int quantidadeDeLivros = bookRepository.findAll().size();
        Assertions.assertThat(quantidadeDeLivros).isEqualTo(2);
    }

    @Test
    void deveBuscarUmLivroPeloIdComSucesso() {
        RestAssured.given()
            .auth()
            .basic("admin1", "test")
            .when()
            .get("/books/1")
            .then()
            .log().all()
            .statusCode(200);
    }
}
