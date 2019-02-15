package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.ListingPage;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.discogs.api.WantlistPage;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class DiscogsClient implements Discogs {

    private final Logger log = getLogger(getClass());
    private final RequestsExecutor executor;
    private final Pages<ListingPage> listingPages;
    private final Pages<WantlistPage> wantlistPages;

    public DiscogsClient(Vertx vertx) {
        listingPages = new Pages<>(new GetListingPage());
        wantlistPages = new Pages<>(new GetWantlistPage());
        executor = new ThrottledRequestsExecutor(vertx);
    }

    @Override
    public Future<List<Listing>> inventory(String user) {
        Future<List<Listing>> future = Future.future();

        listingPages.getFor(user).setHandler(async -> {
           if (async.succeeded()) {
               future.complete(async.result().stream()
                       .map(ListingPage::listings)
                       .flatMap(Collection::stream)
                       .collect(Collectors.toList()));
           } else {
               future.fail(async.cause());
           }
        });

        return future;
    }

    @Override
    public Future<List<Want>> wantlist(String user) {
        Future<List<Want>> future = Future.future();

        wantlistPages.getFor(user).setHandler(async -> {
            if (async.succeeded()) {
                future.complete(async.result().stream()
                        .map(WantlistPage::wants)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
            } else {
                future.fail(async.cause());
            }
        });

        return future;
    }

    private class GetListingPage implements GetPage<ListingPage> {

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

    private class GetWantlistPage implements GetPage<WantlistPage> {

        @Override
        public Future<WantlistPage> apply(String userId, Integer pageNumber) {
            log.info("Request {} wantlist page {}", userId, pageNumber);
            String url = String.format("https://api.discogs.com/users/%s/wants?page=%d", userId, pageNumber);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("User-Agent", "Tradegs/0.1")
                    .GET().build();

            Future<WantlistPage> future = Future.future();
            executor.execute(request).setHandler(async -> {
                if (async.succeeded()) {
                    String json = async.result().body();
                    WantlistPage page = Json.fromJson(json, WantlistPage.class);
                    future.complete(page);
                } else {
                    future.fail(async.cause());
                }
            });
            return future;
        }
    }

}
