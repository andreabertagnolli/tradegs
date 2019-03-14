package ndr.brt.tradegs.discogs;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;

@ExtendWith(VertxExtension.class)
class ThrottledRequestsTest {

    private ThrottledRequests executor;

    @BeforeEach
    void setUp(Vertx vertx) {
        executor = new ThrottledRequests(vertx);

        Router router = Router.router(vertx);
        router.get("/any").handler(it -> it.response().putHeader("X-Discogs-Ratelimit", "500").end("ok!"));
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(1234);
    }

    @Test
    @Timeout(value = 1, timeUnit = SECONDS)
    void handle_throttled_request(VertxTestContext context) {
        Request request = new Request(HttpMethod.GET, "/any", 1234, "localhost");

        executor.execute(request).setHandler(async -> {
            if (async.succeeded()) {
                context.completeNow();
            }
        });
    }

    @Test
    @Timeout(value = 20, timeUnit = SECONDS)
    void handle_many_requests(VertxTestContext context) {
        List<Future> futures = IntStream.range(0, 100)
                .mapToObj(it -> new Request(HttpMethod.GET, "/any", 1234, "localhost"))
                .map(it -> executor.execute(it))
                .collect(Collectors.toList());

        CompositeFuture.all(futures).setHandler(async -> {
            if (async.succeeded()) {
                context.completeNow();
            }
        });

    }

}