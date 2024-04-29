package com.peixoto;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.peixoto.api.TestingWebApplication;
import com.peixoto.api.repository.BookRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(
    initializers = {WireMockInitializer.class}
)
public class BaseIntegrationTest {

    @LocalServerPort
    private int localPort;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
        wireMockServer.resetAll();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
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

    @Test
    void deveRetornarSucessoQuandoConectadoComApiExterna() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/users?page=2"))
                .willReturn(WireMock.okJson("{\"page\":2,\"per_page\":6,\"total\":12,\"total_pages\":2,\"data\":[{\"id\":7,\"email\":\"michael.lawson@reqres.in\",\"first_name\":\"Michael\",\"last_name\":\"Lawson\",\"avatar\":\"https://reqres.in/img/faces/7-image.jpg\"},{\"id\":8,\"email\":\"lindsay.ferguson@reqres.in\",\"first_name\":\"Lindsay\",\"last_name\":\"Ferguson\",\"avatar\":\"https://reqres.in/img/faces/8-image.jpg\"},{\"id\":9,\"email\":\"tobias.funke@reqres.in\",\"first_name\":\"Tobias\",\"last_name\":\"Funke\",\"avatar\":\"https://reqres.in/img/faces/9-image.jpg\"},{\"id\":10,\"email\":\"byron.fields@reqres.in\",\"first_name\":\"Byron\",\"last_name\":\"Fields\",\"avatar\":\"https://reqres.in/img/faces/10-image.jpg\"},{\"id\":11,\"email\":\"george.edwards@reqres.in\",\"first_name\":\"George\",\"last_name\":\"Edwards\",\"avatar\":\"https://reqres.in/img/faces/11-image.jpg\"},{\"id\":12,\"email\":\"rachel.howell@reqres.in\",\"first_name\":\"Rachel\",\"last_name\":\"Howell\",\"avatar\":\"https://reqres.in/img/faces/12-image.jpg\"}],\"support\":{\"url\":\"https://reqres.in/#support-heading\",\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"}}"))
        );

        RestAssured.given()
            .log().all()
            .auth()
            .basic("admin1", "test")
            .contentType(ContentType.JSON)
            .when()
            .get("/books/external")
            .then()
            .log().all()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroQuandoApiExternaEstiverForaDoAr() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/users?page=2"))
                .willReturn(WireMock.notFound())
        );

        RestAssured.given()
            .log().all()
            .auth()
            .basic("admin1", "test")
            .contentType(ContentType.JSON)
            .when()
            .get("/books/external")
            .then()
            .log().all()
            .statusCode(500);
    }
}
