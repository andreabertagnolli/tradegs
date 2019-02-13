package ndr.brt.tradegs.acceptance;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.TradegsVerticle;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Release;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.integration.EmbeddedMongoDb;
import ndr.brt.tradegs.user.CreateUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(VertxExtension.class)
class TradegsAT {

    private final Logger log = getLogger(getClass());
    private static final int HTTP_PORT = 12345;
    private final Discogs discogs = mock(Discogs.class);
    private HttpClient httpClient;

    @BeforeEach
    void setUp(Vertx vertx) throws ExecutionException, InterruptedException {
        EmbeddedMongoDb.activate();
        httpClient = vertx.createHttpClient();

        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.deployVerticle(new TradegsVerticle(HTTP_PORT, discogs), it -> {
            if (it.succeeded()) {
                future.complete(it.result());
            } else {
                future.completeExceptionally(it.cause());
            }
        });
        future.get();
    }

    @Test
    @Timeout(5000)
    void name(VertxTestContext context) {
        when(discogs.inventory("frank_navetta")).thenReturn(Future.succeededFuture(List.of(new Listing(7642143, new Release(1234)))));
        when(discogs.wantlist("frank_navetta")).thenReturn(Future.succeededFuture(List.of(new Want(4321))));

        when(discogs.inventory("darby_crash")).thenReturn(Future.succeededFuture(List.of(new Listing(32254565, new Release(4321)))));
        when(discogs.wantlist("darby_crash")).thenReturn(Future.succeededFuture(List.of(new Want(1234))));

        httpClient.post(HTTP_PORT, "localhost", "/users")
                .handler(response -> response.bodyHandler(body -> log.info("Response: {}", body)))
                .end(Json.toJson(new CreateUser("darby_crash")));

        httpClient.post(HTTP_PORT, "localhost", "/users")
            .handler(response -> response.bodyHandler(body -> log.info("Response: {}", body)))
            .end(Json.toJson(new CreateUser("frank_navetta")));

        await().atMost(5, SECONDS).untilAsserted(() -> {
            httpClient.get(HTTP_PORT, "localhost", "/matches/frank_navetta")
                .handler(response -> {
                    assertThat(response.statusCode()).isEqualTo(200);
                    response.bodyHandler(body -> {
                        assertThat(body.toString()).startsWith("[");
                        context.completeNow();
                    });
                }).end();
        });
    }
}
