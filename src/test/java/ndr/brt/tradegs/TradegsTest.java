package ndr.brt.tradegs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyMap;
import static ndr.brt.tradegs.Json.toJson;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static spark.Spark.port;

class TradegsTest {

    private static final int PORT = 19283;
    private Commands commands = mock(Commands.class);

    @BeforeAll
    static void initSpark() {
        port(PORT);
    }

    @BeforeEach
    void setUp() {
        new Tradegs(commands).init();
    }

    @Test
    void post_user() {
        given()
            .port(PORT)
            .body(toJson(new CreateUser("user")))
            .post("users")
        .then()
            .statusCode(200);

        verify(commands).post(new CreateUser("user"));
    }

    @Test
    void when_id_is_missing_returns_bad_request() {
        given()
            .port(PORT)
            .body(Json.toJson(emptyMap()))
            .post("users")
        .then()
            .statusCode(500);

        verifyZeroInteractions(commands);
    }
}