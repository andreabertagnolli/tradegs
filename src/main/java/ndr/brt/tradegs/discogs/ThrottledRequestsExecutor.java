package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;
import static java.net.http.HttpClient.newHttpClient;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private final Logger log = getLogger(getClass());
    private AtomicLong throttleMillis = new AtomicLong(1);
    private final HttpClient http;
    private Vertx vertx;

    ThrottledRequestsExecutor(Vertx vertx) {
        this.vertx = vertx;
        http = newHttpClient();
    }

    @Override
    public Future<HttpResponse<String>> execute(HttpRequest request) {
        Future<HttpResponse<String>> future = Future.future();

        long delay = max(1, throttleMillis.get());
        log.info("Wait {} milliseconds before the next request", delay);
        vertx.setTimer(delay, time -> {
            try {
                log.info("Send request: {}", request);
                // TODO: can we make the request async?
                HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

                response.headers().firstValue("x-discogs-ratelimit")
                        .map(NumberUtils::createLong)
                        .map(BigDecimal::valueOf)
                        .map(it -> valueOf(60).divide(it))
                        .map(it -> it.multiply(valueOf(1000)))
                        .map(BigDecimal::longValue)
                        .ifPresent(it -> throttleMillis.set(it));

                future.complete(response);
            } catch (Exception e) {
                log.error("Error http request", e);
                future.fail(e);
            }
        });

        return future;
    }
}
