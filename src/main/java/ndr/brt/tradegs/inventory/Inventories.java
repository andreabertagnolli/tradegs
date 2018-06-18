package ndr.brt.tradegs.inventory;

import java.util.List;

public interface Inventories {

    static Inventories inventories() {
        return new DbInventories();
    }

    void save(String id, List<Listing> listings);
}
