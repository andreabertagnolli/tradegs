package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpClient;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.ListingPage;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.discogs.api.WantlistPage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.incubator.http.HttpClient.newHttpClient;

public class DiscogsClient implements Discogs {

    private final HttpClient http;

    public DiscogsClient() {
        http = newHttpClient();
    }

    @Override
    public List<Listing> inventory(String userId) {
        return new ListingPages(userId, http).stream()
                .map(ListingPage::listings)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Want> wantlist(String userId) {
        return new WantsPages(userId, http).stream()
                .map(WantlistPage::wants)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
