package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.api.Want;

import java.util.List;

public interface Wantlists {
    static Wantlists wantlists() {
        return new DbWantlists();
    }

    void save(String id, List<Want> wants);

    List<Want> get(String user);
}
