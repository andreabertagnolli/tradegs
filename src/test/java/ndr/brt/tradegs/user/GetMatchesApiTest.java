package ndr.brt.tradegs.user;

import io.restassured.http.ContentType;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.junit5.VertxExtension;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Release;
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
    private final Users users = mock(Users.class);

    @BeforeEach
    void setUp(Vertx vertx) {
        Router router = Router.router(vertx);

        new GetMatchesApi(router, matches, users).run();

        vertx.createHttpServer().requestHandler(router).listen(PORT);
    }

    @Test
    void get_matches() {
        Match match = new Match("anotherUser").get(new Listing(7655483, new Release(1234))).give(new Listing(873215436, new Release(4321)));
        User validUser = new User().created("anUser");
        when(matches.get(validUser)).thenReturn(List.of(match));
        when(users.get("anUser")).thenReturn(validUser);

        given()
            .port(PORT)
        .when()
            .get("/matches/{userId}", "anUser")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("[0].with", is("anotherUser"))
            .body("[0].get[0].release.id", is(1234))
            .body("[0].give[0].release.id", is(4321))
        ;
    }

    @Test
    void when_user_does_not_exists_returns_400() {
        when(users.get("unexistentUser")).thenReturn(new User());

        given()
            .port(PORT)
        .when()
            .get("/matches/{userId}", "unexistentUser")
        .then()
            .statusCode(404)
            .body(is("User unexistentUser does not exists"))
        ;
    }
}