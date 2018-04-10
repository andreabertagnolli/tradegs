package ndr.brt.tradegs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static ndr.brt.tradegs.Json.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
}