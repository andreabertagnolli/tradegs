package ndr.brt.tradegs.inventory;

import java.util.List;

public class Inventory {
    private String id;
    private List<Listing> listings;

    public List<Listing> listings() {
        return listings;
    }
}
