package ndr.brt.tradegs.discogs;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicLong;

import static java.math.BigDecimal.valueOf;
import static java.net.http.HttpClient.newHttpClient;
import static org.slf4j.LoggerFactory.getLogger;

public class ThrottledRequestsExecutor implements RequestsExecutor {

    private final Logger log = getLogger(getClass());
    private AtomicLong throttleMillis = new AtomicLong(0);
    private final HttpClient http;

    public ThrottledRequestsExecutor() {
        http = newHttpClient();
    }

    @Override
    public HttpResponse<String> execute(HttpRequest request) {
        try {
            log.info("Wait {} milliseconds before the next call", throttleMillis.get());
            Thread.sleep(throttleMillis.get());
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            response.headers().firstValue("x-discogs-ratelimit")
                    .map(NumberUtils::createLong)
                    .map(BigDecimal::valueOf)
                    .map(it -> valueOf(60).divide(it))
                    .map(it -> it.multiply(valueOf(1000)))
                    .map(BigDecimal::longValue)
                    .ifPresent(it -> throttleMillis.set(it));

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
