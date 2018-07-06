package ndr.brt.tradegs.wantlist;

import java.util.List;

public interface Wantlists {
    static Wantlists wantlists() {
        return new DbWantlists();
    }

    void save(String id, List<Want> wants);
}
