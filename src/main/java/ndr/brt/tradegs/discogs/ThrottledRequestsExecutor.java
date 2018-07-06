package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import static java.math.BigDecimal.valueOf;
import static jdk.incubator.http.HttpClient.newHttpClient;
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
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandler.asString());

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
