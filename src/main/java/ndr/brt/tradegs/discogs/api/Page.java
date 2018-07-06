package ndr.brt.tradegs.discogs.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

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

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
