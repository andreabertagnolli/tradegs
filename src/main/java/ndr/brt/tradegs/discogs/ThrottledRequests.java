package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import org.slf4j.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequests implements Requests {

    private final Logger log = getLogger(getClass());
    private final BlockingQueue<RequestContext> queue = new LinkedBlockingQueue<>();
    private final AtomicLong executorId = new AtomicLong(0);
    private final AtomicLong actualDelay = new AtomicLong(100);
    private final io.vertx.core.http.HttpClient httpClient;

    private Vertx vertx;

    ThrottledRequests(Vertx vertx) {
        this.vertx = vertx;
        this.httpClient = vertx.createHttpClient();
        long id = vertx.setPeriodic(actualDelay.get(), executor());
        this.executorId.set(id);
    }

    @Override
    public Future<Buffer> execute(Request request) {
        try {
            Future<Buffer> future = Future.future();
            queue.put(new RequestContext(request, future));
            return future;
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    private Handler<Long> executor() {
        log.info("Instancing new request executor with throttled delay of {} ms", actualDelay.get());
        return timerId -> {
            RequestContext context = queue.poll();
            if (context != null) {
                Request inputRequest = context.request;
                log.info("Send request: {} {}", inputRequest.method(), inputRequest.options().getURI());
                httpClient.request(inputRequest.method(), inputRequest.options())
                    .putHeader("User-Agent", "Tradegs/0.1")
                    .setFollowRedirects(true)
                    .handler(response -> {
                        response.bodyHandler(context.future::complete);
                        checkAndUpdateRateLimit(response);
                    })
                    .end();

            }
        };
    }

    private void checkAndUpdateRateLimit(HttpClientResponse response) {
        Optional.ofNullable(response.getHeader("X-Discogs-Ratelimit"))
            .map(Long::parseLong)
            .map(rateLimit -> rateLimit - 1)
            .map(requestsPerMinute -> 60000 / requestsPerMinute)
            .ifPresent(throttleDelay -> {
                if (throttleDelay != actualDelay.getAndSet(throttleDelay)) {
                    vertx.cancelTimer(executorId.get());
                    long id = vertx.setPeriodic(throttleDelay, executor());
                    executorId.set(id);
                }
            });
    }

    private static class RequestContext {
        private final Request request;
        private final Future<Buffer> future;

        private RequestContext(Request request, Future<Buffer> future) {
            this.request = request;
            this.future = future;
        }
    }

}
