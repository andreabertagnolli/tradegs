package ndr.brt.tradegs.discogs;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ExtendWith(VertxExtension.class)
class ThrottledRequestsExecutorTest {

    private ThrottledRequestsExecutor executor;

    @BeforeEach
    void setUp(Vertx vertx) {
        executor = new ThrottledRequestsExecutor(vertx, 20);

        Router router = Router.router(vertx);
        router.get("/any").handler(it -> it.response().end("ok!"));
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(1234);
    }

    @Test
    @Timeout(value = 1, timeUnit = SECONDS)
    void handle_throttled_request(VertxTestContext context) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:1234/any")).build();

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
                .mapToObj(it -> HttpRequest.newBuilder().uri(URI.create("http://localhost:1234/any")).build())
                .map(it -> executor.execute(it))
                .collect(Collectors.toList());

        CompositeFuture.all(futures).setHandler(async -> {
            if (async.succeeded()) {
                context.completeNow();
            }
        });

    }



}