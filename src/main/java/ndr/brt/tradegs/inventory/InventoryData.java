package ndr.brt.tradegs.inventory;

import java.util.List;

public class InventoryData {
    private String id;
    private List<Listing> listings;

    public List<Listing> listings() {
        return listings;
    }
}
