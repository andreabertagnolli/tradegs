package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;
import static java.net.http.HttpClient.newHttpClient;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private static final int THROTTLE_DELAY = 2400;
    private final Logger log = getLogger(getClass());
    private final HttpClient http;
    private BlockingQueue<RequestHandler> queue;

    ThrottledRequestsExecutor(Vertx vertx) {
        queue = new LinkedBlockingQueue<>();
        http = newHttpClient();

        vertx.setPeriodic(THROTTLE_DELAY, time -> {
            RequestHandler handler = queue.poll();
            if (handler != null) {
                log.info("Send request: {}", handler.request);
                http.sendAsync(handler.request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(handler.future::complete);
            }
        });
    }

    @Override
    public Future<HttpResponse<String>> execute(HttpRequest request) {
        try {
            Future<HttpResponse<String>> future = Future.future();
            queue.put(new RequestHandler(request, future));
            return future;
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    private static class RequestHandler {
        private final HttpRequest request;
        private final Future<HttpResponse<String>> future;

        private RequestHandler(HttpRequest request, Future<HttpResponse<String>> future) {
            this.request = request;
            this.future = future;
        }
    }
}
