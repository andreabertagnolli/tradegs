package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.discogs.api.WantlistPage;
import org.slf4j.Logger;

import java.net.URI;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.slf4j.LoggerFactory.getLogger;

public class WantsPages implements Iterable<WantlistPage> {

    private final Logger log = getLogger(getClass());
    private final String utente;
    private final HttpClient http;

    public WantsPages(String utente, HttpClient httpClient) {
        this.utente = utente;
        http = httpClient;
    }

    @Override
    public Iterator<WantlistPage> iterator() {
        return new WantlistPagesIterator();
    }

    public Stream<WantlistPage> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    private class WantlistPagesIterator implements Iterator<WantlistPage> {

        private boolean hasNext;
        private int actualPage;

        private WantlistPagesIterator() {
            this.hasNext = true;
            this.actualPage = 1;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public WantlistPage next() {
            log.info("Request inventory page {}", actualPage);
            String url = String.format("https://api.discogs.com/users/%s/wants?page=%d", utente, actualPage);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("User-Agent", "Tradegs")
                    .GET().build();
            try {
                HttpResponse<String> response = http.send(request, HttpResponse.BodyHandler.asString());

                String json = response.body();

                WantlistPage page = Json.fromJson(json, WantlistPage.class);

                hasNext = page.page() < page.pages();
                actualPage++;

                return page;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
