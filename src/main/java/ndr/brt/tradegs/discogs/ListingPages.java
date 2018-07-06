package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.api.ListingPage;
import org.slf4j.Logger;

import java.net.URI;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.slf4j.LoggerFactory.getLogger;

public class ListingPages implements Iterable<ListingPage> {

    private final Logger log = getLogger(getClass());
    private final String utente;
    private final RequestsExecutor executor;

    public ListingPages(String utente, RequestsExecutor executor) {
        this.utente = utente;
        this.executor = executor;
    }

    @Override
    public Iterator<ListingPage> iterator() {
        return new ListingPagesIterator();
    }

    public Stream<ListingPage> stream() {
        return StreamSupport.stream(this.spliterator(), false);
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
                HttpResponse<String> response = executor.execute(request);

                String json = response.body();

                ListingPage listingPage = Json.fromJson(json, ListingPage.class);

                hasNext = listingPage.page() < listingPage.pages();
                page++;

                return listingPage;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
