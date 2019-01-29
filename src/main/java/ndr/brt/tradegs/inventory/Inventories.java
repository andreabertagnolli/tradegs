package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.api.Listing;

import java.util.List;

public interface Inventories {

    static Inventories inventories() {
        return new DbInventories();
    }

    void save(String id, List<Listing> listings);

    List<Listing> get(String user);
}
