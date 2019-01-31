package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;

import java.util.List;
import java.util.UUID;

public class DiscogsInventoryClient implements InventoryClient {
    private final Discogs discogs;
    private final IdGenerator idGenerator;
    private final Inventories inventories;

    public DiscogsInventoryClient(Discogs discogs, IdGenerator idGenerator, Inventories inventories) {
        this.discogs = discogs;
        this.idGenerator = idGenerator;
        this.inventories = inventories;
    }

    @Override
    public String fetch(String user) {
        List<Listing> listings = discogs.inventory(user);
        String inventoryId = idGenerator.generate();
        inventories.save(user, listings);
        return inventoryId;
    }
}
