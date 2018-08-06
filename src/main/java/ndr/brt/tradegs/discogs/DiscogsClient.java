package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.ListingPage;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.discogs.api.WantlistPage;
import org.slf4j.Logger;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class DiscogsClient implements Discogs {

    private final Logger log = getLogger(getClass());
    private final RequestsExecutor executor = new ThrottledRequestsExecutor();
    private final Pages<ListingPage> listingPages;
    private final Pages<WantlistPage> wantlistPages;

    public DiscogsClient() {
        listingPages = new Pages<>(getListingPage);
        wantlistPages = new Pages<>(getWantlistPage);
    }

    @Override
    public List<Listing> inventory(String userId) {
        return listingPages
                .getFor(userId)
                .map(ListingPage::listings)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Want> wantlist(String userId) {
        return wantlistPages
                .getFor(userId)
                .map(WantlistPage::wants)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private final GetPage<ListingPage> getListingPage = (userId, pageNumber) -> {
        log.info("Request inventory page {}", pageNumber);
        String url = String.format("https://api.discogs.com/users/%s/inventory?page=%d", userId, pageNumber);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Tradegs")
                .GET().build();

        String json = executor
                .execute(request)
                .body();

        return Json.fromJson(json, ListingPage.class);
    };

    private final GetPage<WantlistPage> getWantlistPage = (userId, pageNumber) -> {
        log.info("Request wantlist page {}", pageNumber);
        String url = String.format("https://api.discogs.com/users/%s/wants?page=%d", userId, pageNumber);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Tradegs")
                .GET().build();

        String json = executor
                .execute(request)
                .body();

        return Json.fromJson(json, WantlistPage.class);
    };

}
