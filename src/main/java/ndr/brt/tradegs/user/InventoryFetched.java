package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Event;
import ndr.brt.tradegs.inventory.Listing;

import java.util.List;

public class InventoryFetched extends Event {
    public InventoryFetched(String userId, List<Listing> listings) {
        super();
    }
}
