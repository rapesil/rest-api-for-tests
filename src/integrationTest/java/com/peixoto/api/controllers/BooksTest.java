package com.peixoto.api.controllers;

import com.peixoto.api.domain.Book;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.utils.BookFactory;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureWireMock(port = 0)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BooksTest {

    @Autowired
    private BookRepository bookRepository;

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

    @Test
    void getById_shouldReturnAnSpecificBook() {
        final long VALID_ID = 1L;
        Book book = given()
            .auth().basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .pathParam("id", VALID_ID)
        .when()
            .get("/{id}")
        .then()
            .statusCode(200)
            .extract().body().as(Book.class);

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("BOOK TO BE REPLACED");
        assertThat(book.getAuthor()).isEqualTo("AUTHOR TO BE REPLACED");
        assertThat(book.getBookCategory()).isEqualTo("CATEGORY TO BE REPLACED");
    }

    @Test
    void post_shouldInsertNewBook()  {
        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
            .body(BookFactory.getBookWithoutId())
        .when()
            .post("/" )
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_CREATED);

        assertThat(bookRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void put_shouldReplaceABook() {
        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
            .body(BookFactory.getBookWithId())
        .when()
            .put("/" )
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_NO_CONTENT);

        assertThat(bookRepository.findAll().size()).isEqualTo(2);
        assertThat(bookRepository.findById(1L).get().getTitle()).isEqualTo("Selenium WebDriver");
        assertThat(bookRepository.findById(1L).get().getAuthor()).isEqualTo("Rafael Peixoto");
        assertThat(bookRepository.findById(1L).get().getBookCategory()).isEqualTo("Software Test");
    }

    @Test
    void put_shouldThrowNotFound_whenBookDoesNotExists() {
        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
            .body(BookFactory.getBookWithInvalidId())
        .when()
            .put("/" )
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void delete_shouldRemoveBook() {
        final Long ID_TO_BE_DELETED = 2L;

        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
            .pathParam("id", ID_TO_BE_DELETED)
        .when()
            .delete("/{id}")
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_NO_CONTENT);

        assertThat(bookRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void delete_shouldThrowBadRequest_whenInvalidIdIsSent() {
        final Long INVALID_ID_TO_BE_DELETED = 100L;

        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
            .pathParam("id", INVALID_ID_TO_BE_DELETED)
        .when()
            .delete("/{id}")
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void getExternal_shouldReceveidData() {
        stubFor(get(urlMatching("reqres.in"))
            .willReturn(aResponse().withStatus(200)));

        given()
            .log().all()
            .auth()
            .basic(VALID_USER_ADMIN, VALID_PASS_ADMIN)
            .contentType(ContentType.JSON)
        .when()
            .get("/external")
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

}
