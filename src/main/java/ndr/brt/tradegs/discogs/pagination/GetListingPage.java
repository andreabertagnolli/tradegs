package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.Future;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.discogs.RequestsExecutor;
import ndr.brt.tradegs.discogs.ThrottledRequestsExecutor;
import ndr.brt.tradegs.discogs.api.ListingPage;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;

import static org.slf4j.LoggerFactory.getLogger;

public class GetListingPage implements GetPage<ListingPage> {

    private final Logger log = getLogger(getClass());
    private RequestsExecutor executor;

    public GetListingPage(RequestsExecutor executor) {
        this.executor = executor;
    }

    @Override
    public Future<ListingPage> apply(String userId, Integer pageNumber) {
        log.info("Request {} inventory page {}", userId, pageNumber);
        String url = String.format("https://api.discogs.com/users/%s/inventory?page=%d", userId, pageNumber);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Tradegs/0.1")
                .GET().build();

        Future<ListingPage> future = Future.future();
        executor.execute(request).setHandler(async -> {
            if (async.succeeded()) {
                String json = async.result().body();
                ListingPage page = Json.fromJson(json, ListingPage.class);
                future.complete(page);
            } else {
                future.fail(async.cause());
            }
        });

        return future;
    }
}
