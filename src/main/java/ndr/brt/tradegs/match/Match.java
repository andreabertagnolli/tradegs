package ndr.brt.tradegs.match;

import ndr.brt.tradegs.discogs.api.Listing;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private final List<Listing> userOne = new ArrayList<>();
    private final List<Listing> userTwo = new ArrayList<>();

    public Match userOne(Listing listing) {
        userOne.add(listing);
        return this;
    }

    public Match userTwo(Listing listing) {
        userTwo.add(listing);
        return this;
    }
}
