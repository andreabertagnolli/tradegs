package ndr.brt.tradegs.user;

import io.restassured.http.ContentType;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.junit5.VertxExtension;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.match.Match;
import ndr.brt.tradegs.match.Matches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class GetMatchesApiTest {

    private static final int PORT = 41431;
    private final Matches matches = mock(Matches.class);

    @BeforeEach
    void setUp(Vertx vertx) {
        Router router = Router.router(vertx);

        new GetMatchesApi(router, matches).run();

        vertx.createHttpServer().requestHandler(router).listen(PORT);
    }

    @Test
    void get_matches() {
        Match match = new Match("anotherUser").get(new Listing(1234)).give(new Listing(4321));
        when(matches.get("anUser")).thenReturn(List.of(match));

        given()
            .port(PORT)
        .when()
            .get("/matches/{userId}", "anUser")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("[0].with", is("anotherUser"))
            .body("[0].get[0].id", is(1234))
            .body("[0].give[0].id", is(4321))
        ;

        verify(matches).get("anUser");
    }
}