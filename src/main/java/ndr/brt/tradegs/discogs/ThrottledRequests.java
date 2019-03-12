package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequests implements Requests {

    private final Logger log = getLogger(getClass());
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final BlockingQueue<RequestContext> queue = new LinkedBlockingQueue<>();
    private final AtomicLong executorId = new AtomicLong(0);
    private final AtomicLong actualDelay = new AtomicLong(100);

    private Vertx vertx;

    ThrottledRequests(Vertx vertx) {
        this.vertx = vertx;
        executorId.set(vertx.setPeriodic(actualDelay.get(), executor()));
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

    private Handler<Long> executor() {
        log.info("Instancing new request executor with throttled delay of {} ms", actualDelay.get());
        return timerId -> {
            RequestContext context = queue.poll();
            if (context != null) {
                log.info("Send request: {}", context.request);
                http.sendAsync(context.request, ofString())
                    .thenApply(response -> {
                        response.headers().firstValue("X-Discogs-Ratelimit")
                            .map(Long::parseLong)
                            .map(rateLimit -> rateLimit - 1)
                            .map(requestsPerMinute -> 60000 / requestsPerMinute)
                            .ifPresent(throttleDelay -> {
                                if (throttleDelay != actualDelay.getAndSet(throttleDelay)) {
                                    vertx.cancelTimer(executorId.get());
                                    executorId.set(vertx.setPeriodic(actualDelay.get(), executor()));
                                }
                            });
                        return response;
                    })
                    .thenAccept(context.future::complete);
            }
        };
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
