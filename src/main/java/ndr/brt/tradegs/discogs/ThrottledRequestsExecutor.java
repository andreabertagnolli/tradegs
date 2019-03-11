package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import org.slf4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private static final int DEFAULT_DELAY = 2500;
    private final Logger log = getLogger(getClass());
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final BlockingQueue<RequestContext> queue = new LinkedBlockingQueue<>();
    private final AtomicLong timer = new AtomicLong(0);
    private final Handler<Long> executor = timerId -> {
        RequestContext context = queue.poll();
        if (context != null) {
            log.info("Send request: {}", context.request);
            http.sendAsync(context.request, ofString())
                    .thenAccept(context.future::complete);
        }
    };

    ThrottledRequestsExecutor(Vertx vertx) {
        this(vertx, DEFAULT_DELAY);
    }

    ThrottledRequestsExecutor(Vertx vertx, int delay) {
        timer.set(vertx.setPeriodic(delay, executor));
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
