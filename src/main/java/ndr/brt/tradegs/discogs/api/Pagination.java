package ndr.brt.tradegs.discogs.api;

import java.util.Map;

class Pagination {
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

    public int page() {
        return page;
    }

    public int pages() {
        return pages;
    }
}
