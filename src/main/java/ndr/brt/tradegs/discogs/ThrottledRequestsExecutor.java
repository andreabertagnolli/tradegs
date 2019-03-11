package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private static final int DEFAULT_DELAY = 2500;
    private final Logger log = getLogger(getClass());
    private final HttpClient http;
    private BlockingQueue<RequestContext> queue;

    ThrottledRequestsExecutor(Vertx vertx) {
        this(vertx, DEFAULT_DELAY);
    }

    ThrottledRequestsExecutor(Vertx vertx, int delay) {
        queue = new LinkedBlockingQueue<>();
        http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

        vertx.setPeriodic(delay, time -> {
            RequestContext context = queue.poll();
            if (context != null) {
                log.info("Send request: {}", context.request);
                http.sendAsync(context.request, ofString())
                    .thenAccept(it -> {
                        log.info("AAAAAAAAAAAAAAAAAAA Received response {} to request {}. Headers: {}", it.statusCode(), context.request, it.headers());
                        context.future.complete(it);
                    });
            }
        });
    }

    @Override
    public Future<HttpResponse<String>> execute(HttpRequest request) {
        try {
            Future<HttpResponse<String>> future = Future.future();
            queue.put(new RequestContext(request, future));
            return future;
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    private static class RequestContext {
        private final HttpRequest request;
        private final Future<HttpResponse<String>> future;

        private RequestContext(HttpRequest request, Future<HttpResponse<String>> future) {
            this.request = request;
            this.future = future;
        }
    }
}
