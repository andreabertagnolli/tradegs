package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Event;

public class InventoryFetched extends Event {
    private final String userId;
    private final String inventoryId;

    public InventoryFetched(String userId, String inventoryId) {
        this.userId = userId;
        this.inventoryId = inventoryId;
    }

    public String inventoryId() {
        return inventoryId;
    }
}
