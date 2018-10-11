package ndr.brt.tradegs;

import ndr.brt.tradegs.user.CreateUser;
import ndr.brt.tradegs.user.CreateUserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyMap;
import static ndr.brt.tradegs.Json.toJson;
import static org.mockito.Mockito.*;

class CreateUserResourceTest extends ResourceTest {

    private Commands commands = mock(Commands.class);

    @BeforeEach
    void setUp() {
        new CreateUserResource(commands).run();
    }

    @Test
    void post_user() {
        given()
            .port(LISTEN_PORT)
            .body(toJson(new CreateUser("user")))
            .post("users")
        .then()
            .statusCode(200);

        verify(commands).post(isA(CreateUser.class));
        verify(commands).post(argThat(it -> "user".equals(((CreateUser) it).id().orElse(null))));
    }

    @Test
    void when_id_is_missing_returns_bad_request() {
        given()
            .port(LISTEN_PORT)
            .body(Json.toJson(emptyMap()))
            .post("users")
        .then()
            .statusCode(500);

        verifyZeroInteractions(commands);
    }
}