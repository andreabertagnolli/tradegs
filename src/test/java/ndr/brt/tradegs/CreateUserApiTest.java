package ndr.brt.tradegs;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.user.CreateUser;
import ndr.brt.tradegs.user.CreateUserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyMap;
import static ndr.brt.tradegs.Json.toJson;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class CreateUserApiTest {

    private static final int PORT = 41231;
    private Bus commands = mock(Bus.class);

    @BeforeEach
    void setUp(Vertx vertx) {
        Router router = Router.router(vertx);

        new CreateUserApi(router, commands).run();

        vertx.createHttpServer().requestHandler(router).listen(PORT);
    }

    @Test
    @Timeout(1000)
    void post_user(VertxTestContext context) {
        given()
            .port(PORT)
            .body(toJson(new CreateUser("user")))
            .post("/users")
        .then()
            .statusCode(200);

        verify(commands).publish(isA(CreateUser.class));
        verify(commands).publish(argThat(it -> "user".equals(((CreateUser) it).id().orElse(null))));
        context.completeNow();
    }

    @Test
    @Timeout(1000)
    void when_id_is_missing_returns_bad_request(VertxTestContext context) {
        given()
            .port(PORT)
            .body(Json.toJson(emptyMap()))
            .post("/users")
        .then()
            .statusCode(500);

        verifyZeroInteractions(commands);
        context.completeNow();
    }
}