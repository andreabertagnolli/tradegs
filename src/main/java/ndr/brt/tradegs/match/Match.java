package ndr.brt.tradegs.match;

import ndr.brt.tradegs.discogs.api.Listing;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private final List<Listing> get = new ArrayList<>();
    private final List<Listing> give = new ArrayList<>();
    private String with;

    public Match(String with) {
        this.with = with;
    }

    public Match get(Listing listing) {
        get.add(listing);
        return this;
    }

    public Match give(Listing listing) {
        give.add(listing);
        return this;
    }

    public Match give(List<Listing> give) {
        this.give.addAll(give);
        return this;
    }

    public Match get(List<Listing> get) {
        this.get.addAll(get);
        return this;
    }
}
