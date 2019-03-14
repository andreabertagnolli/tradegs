package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.Future;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.Request;
import ndr.brt.tradegs.discogs.Requests;
import ndr.brt.tradegs.discogs.api.WantlistPage;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

import static org.slf4j.LoggerFactory.getLogger;

public class GetWantlistPage implements GetPage<WantlistPage> {

    private final Logger log = getLogger(getClass());
    private Requests executor;

    public GetWantlistPage(Requests executor) {
        this.executor = executor;
    }

    @Override
    public Future<WantlistPage> apply(String userId, Integer pageNumber) {
        log.info("Request {} wantlist page {}", userId, pageNumber);
        String url = String.format("https://api.discogs.com/users/%s/wants?page=%d", userId, pageNumber);
        Request request = Request.get(url).header("User-Agent", "Tradegs/0.1");

        Future<WantlistPage> future = Future.future();
        executor.execute(request).setHandler(async -> {
            if (async.succeeded()) {
                String json = async.result().toString();
                WantlistPage page = Json.fromJson(json, WantlistPage.class);
                future.complete(page);
            } else {
                future.fail(async.cause());
            }
        });
        return future;
    }
}
