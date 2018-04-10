package ndr.brt.tradegs;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class TradegsTest {

    private static final int PORT = 19283;

    @BeforeAll
    static void setUp() {
        Spark.port(PORT);
        new Tradegs().init();
    }

    @Test
    void post_user() {
        given()
            .port(PORT)
            .body("{\"id\":\"user\"}")
            .post("users")
        .then()
            .statusCode(200);
    }
}