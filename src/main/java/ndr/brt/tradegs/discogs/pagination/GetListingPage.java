package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.Future;
import io.vertx.core.http.RequestOptions;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.Request;
import ndr.brt.tradegs.discogs.Requests;
import ndr.brt.tradegs.discogs.api.ListingPage;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

import static org.slf4j.LoggerFactory.getLogger;

public class GetListingPage implements GetPage<ListingPage> {

    private final Logger log = getLogger(getClass());
    private Requests executor;

    public GetListingPage(Requests executor) {
        this.executor = executor;
    }

    @Override
    public Future<ListingPage> apply(String userId, Integer pageNumber) {
        log.info("Request {} inventory page {}", userId, pageNumber);
        Request request = Request.get(String.format("/users/%s/inventory?page=%d", userId, pageNumber));

        Future<ListingPage> future = Future.future();
        executor.execute(request).setHandler(async -> {
            if (async.succeeded()) {
                String json = async.result().toString();
                ListingPage page = Json.fromJson(json, ListingPage.class);
                future.complete(page);
            } else {
                future.fail(async.cause());
            }
        });

        return future;
    }
}
