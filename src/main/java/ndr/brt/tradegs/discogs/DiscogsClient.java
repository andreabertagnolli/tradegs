package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.ListingPage;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.discogs.api.WantlistPage;
import ndr.brt.tradegs.discogs.pagination.GetListingPage;
import ndr.brt.tradegs.discogs.pagination.GetWantlistPage;
import ndr.brt.tradegs.discogs.pagination.Pages;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DiscogsClient implements Discogs {

    private final Pages<ListingPage> listingPages;
    private final Pages<WantlistPage> wantlistPages;

    public DiscogsClient(Vertx vertx) {
        Requests executor = new ThrottledRequests(vertx);
        listingPages = new Pages<>(new GetListingPage(executor));
        wantlistPages = new Pages<>(new GetWantlistPage(executor));
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

}
