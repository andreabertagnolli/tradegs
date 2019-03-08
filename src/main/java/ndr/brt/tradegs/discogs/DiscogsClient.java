package ndr.brt.tradegs.discogs;

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
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class DiscogsClient implements Discogs {

    private final Pages<ListingPage> listingPages;
    private final Pages<WantlistPage> wantlistPages;

    public DiscogsClient(Vertx vertx) {
        RequestsExecutor executor = new ThrottledRequestsExecutor(vertx);
        listingPages = new Pages<>(new GetListingPage(executor));
        wantlistPages = new Pages<>(new GetWantlistPage(executor));
    }

    @Override
    public CompletableFuture<List<Listing>> inventory(String user) {
        return listingPages.getFor(user)
            .thenApply(it -> it.stream().map(ListingPage::listings).flatMap(Collection::stream).collect(toList()));
    }

    @Override
    public CompletableFuture<List<Want>> wantlist(String user) {
        return wantlistPages.getFor(user)
            .thenApply(pages -> pages.stream().map(WantlistPage::wants).flatMap(Collection::stream).collect(toList()));
    }

}
