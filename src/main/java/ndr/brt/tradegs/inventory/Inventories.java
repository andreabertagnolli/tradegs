package ndr.brt.tradegs.inventory;

import java.util.List;

public interface Inventories {

    void save(String id, List<Listing> listings);
}
