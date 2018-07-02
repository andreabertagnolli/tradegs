package ndr.brt.tradegs.discogs.api;

import java.util.List;

public class ListingPage {
    private final Pagination pagination;
    private final List<Listing> listings;

    private ListingPage(Pagination pagination, List<Listing> listings) {
        this.pagination = pagination;
        this.listings = listings;
    }

    public List<Listing> listings() {
        return listings;
    }

    public int page() {
        return pagination.page();
    }

    public int pages() {
        return pagination.pages();
    }
}
