package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.api.Listing;

import java.util.List;

public interface Wantlists {
    static Wantlists wantlists() {
        return null; // TODO: implement wantlists
    }

    void save(String id, List<Want> wants);
}
