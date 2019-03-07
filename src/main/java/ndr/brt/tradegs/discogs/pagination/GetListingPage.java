package ndr.brt.tradegs.discogs.pagination;

import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.RequestsExecutor;
import ndr.brt.tradegs.discogs.api.ListingPage;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.slf4j.LoggerFactory.getLogger;

public class GetListingPage implements GetPage<ListingPage> {

    private final Logger log = getLogger(getClass());
    private RequestsExecutor executor;

    public GetListingPage(RequestsExecutor executor) {
        this.executor = executor;
    }

    @Override
    public CompletableFuture<ListingPage> apply(String userId, Integer pageNumber) {
        log.info("Request {} inventory page {}", userId, pageNumber);
        String url = String.format("https://api.discogs.com/users/%s/inventory?page=%d", userId, pageNumber);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Tradegs/0.1")
                .GET().build();

        return executor.execute(request)
                .thenApply(HttpResponse::body)
                .thenApply(it -> Json.fromJson(it, ListingPage.class));
    }
}
