package ndr.brt.tradegs.discogs.api;

import java.util.List;

public class ListingPage extends Page {
    private final List<Listing> listings;

    private ListingPage(Pagination pagination, List<Listing> listings) {
        super(pagination);
        this.listings = listings;
    }

    public List<Listing> listings() {
        return listings;
    }

}
