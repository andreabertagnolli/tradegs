package ndr.brt.tradegs.inventory;

import jdk.incubator.http.HttpClient;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.ListingPage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jdk.incubator.http.HttpClient.newHttpClient;

public class DiscogsClient implements Discogs {

    private final HttpClient http;

    public DiscogsClient() {
        http = newHttpClient();
    }

    @Override
    public List<Listing> inventory(String user) {

        ListingPages pages = new ListingPages(user, http);
        return StreamSupport.stream(pages.spliterator(), false)
                .map(ListingPage::listings)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
