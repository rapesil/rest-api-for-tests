package com.peixoto.api;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Tag("integrationTest")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestingWebApplication.class
)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
//@AutoConfigureWireMock(port = 0)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    protected int getPort(){
        return port;
    }
}
