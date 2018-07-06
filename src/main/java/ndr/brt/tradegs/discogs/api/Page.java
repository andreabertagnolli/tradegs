package ndr.brt.tradegs.discogs.api;

public abstract class Page {
    private final Pagination pagination;

    protected Page(Pagination pagination) {
        this.pagination = pagination;
    }

    public int page() {
        return pagination.page();
    }

    public int pages() {
        return pagination.pages();
    }
}
