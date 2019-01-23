package ndr.brt.tradegs.discogs;

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
    private final RequestsExecutor executor = new ThrottledRequestsExecutor();
    private final Pages<ListingPage> listingPages;
    private final Pages<WantlistPage> wantlistPages;

    public DiscogsClient() {
        listingPages = new Pages<>(new GetListingPage());
        wantlistPages = new Pages<>(new GetWantlistPage());
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

    private class GetListingPage implements GetPage<ListingPage> {

        @Override
        public ListingPage apply(String userId, Integer pageNumber) {
            log.info("Request inventory page {}", pageNumber);
            String url = String.format("https://api.discogs.com/users/%s/inventory?page=%d", userId, pageNumber);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("User-Agent", "Tradegs")
                    .GET().build();

            String json = executor
                    .execute(request)
                    .body();

            return Json.fromJson(json, ListingPage.class);
        }
    }

    private class GetWantlistPage implements GetPage<WantlistPage> {

        @Override
        public WantlistPage apply(String userId, Integer pageNumber) {
            log.info("Request wantlist page {}", pageNumber);
            String url = String.format("https://api.discogs.com/users/%s/wants?page=%d", userId, pageNumber);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("User-Agent", "Tradegs")
                    .GET().build();

            String json = executor
                    .execute(request)
                    .body();

            return Json.fromJson(json, WantlistPage.class);
        }
    }

}
