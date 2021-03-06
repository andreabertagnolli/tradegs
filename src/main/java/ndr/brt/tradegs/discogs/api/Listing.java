package ndr.brt.tradegs.discogs.api;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Listing {
    private final int id;
    private final Release release;

    public Listing(int id, Release release) {
        this.id = id;
        this.release = release;
    }

    public int id() {
        return id;
    }

    public Release release() {
        return release;
    }

    @Override
    public boolean equals(Object that) {
        return reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
