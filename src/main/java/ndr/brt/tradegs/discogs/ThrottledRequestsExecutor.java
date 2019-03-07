package ndr.brt.tradegs.discogs;

import io.vertx.core.Vertx;
import org.slf4j.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static java.net.http.HttpClient.newHttpClient;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private static final int DEFAULT_DELAY = 2400;
    private final Logger log = getLogger(getClass());
    private final HttpClient http;
    private BlockingQueue<RequestContext> queue;

    ThrottledRequestsExecutor(Vertx vertx) {
        this(vertx, DEFAULT_DELAY);
    }

    ThrottledRequestsExecutor(Vertx vertx, int delay) {
        queue = new LinkedBlockingQueue<>();
        http = newHttpClient();

        vertx.setPeriodic(delay, time -> {
            RequestContext context = queue.poll();
            if (context != null) {
                log.info("Send request: {}", context.request);
                http.sendAsync(context.request, HttpResponse.BodyHandlers.ofString())
                        .thenAcceptAsync(context.future::complete);
            }
        });
    }

    @Override
    public CompletableFuture<HttpResponse<String>> execute(HttpRequest request) {
        try {
            CompletableFuture<HttpResponse<String>> future = new CompletableFuture<>();
            queue.put(new RequestContext(request, future));
            return future;
        } catch (Exception e) {
            return failedFuture(e);
        }
    }

    private static class RequestContext {
        private final HttpRequest request;
        private final CompletableFuture<HttpResponse<String>> future;

        private RequestContext(HttpRequest request, CompletableFuture<HttpResponse<String>> future) {
            this.request = request;
            this.future = future;
        }
    }
}
