package ndr.brt.tradegs.inventory;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.Discogs;
import org.slf4j.Logger;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jdk.incubator.http.HttpClient.newHttpClient;
import static org.slf4j.LoggerFactory.getLogger;

public class DiscogsClient implements Discogs {

    private final HttpClient http;

    public DiscogsClient() {
        http = newHttpClient();
    }

    @Override
    public List<Listing> inventory(String user) {

        ListingPages pages = new ListingPages(user, http);
        return StreamSupport.stream(pages.spliterator(), false)
                .map(it -> it.listings)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static class ListingPage {
        private final Pagination pagination;
        private final List<Listing> listings;

        private ListingPage(Pagination pagination, List<Listing> listings) {
            this.pagination = pagination;
            this.listings = listings;
        }
    }

    private static class Pagination {
        private final int per_page;
        private final int items;
        private final int page;
        private final int pages;
        private final Map<String, Object> urls;

        private Pagination(int per_page, int items, int page, int pages, Map<String, Object> urls) {
            this.per_page = per_page;
            this.items = items;
            this.page = page;
            this.pages = pages;
            this.urls = urls;
        }
    }

    private static class ListingPages implements Iterable<ListingPage> {

        private final Logger log = getLogger(getClass());
        private final String utente;
        private final HttpClient http;

        public ListingPages(String utente, HttpClient httpClient) {
            this.utente = utente;
            http = httpClient;
        }

        @Override
        public Iterator<ListingPage> iterator() {
            return new ListingPagesIterator();
        }

        private class ListingPagesIterator implements Iterator<ListingPage> {

            private boolean hasNext;
            private int page;

            private ListingPagesIterator() {
                this.hasNext = true;
                this.page = 1;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public ListingPage next() {
                log.info("Request inventory page {}", page);
                String url = String.format("https://api.discogs.com/users/%s/inventory?page=%d", utente, page);
                HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                        .header("User-Agent", "Tradegs")
                        .GET().build();
                try {
                    HttpResponse<String> response = http.send(request, HttpResponse.BodyHandler.asString());

                    String json = response.body();

                    ListingPage listingPage = Json.fromJson(json, ListingPage.class);

                    hasNext = listingPage.pagination.page < listingPage.pagination.pages;
                    page++;

                    return listingPage;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
