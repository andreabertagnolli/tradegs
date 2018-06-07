package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.Discogs;

import java.util.List;
import java.util.UUID;

public class DiscogsInventory implements Inventory {
    private final Discogs discogs;
    private final IdGenerator idGenerator;
    private final Inventories inventories;

    public DiscogsInventory() {
        this(null, () -> UUID.randomUUID().toString(), null); // TODO: implement discogs interface and inventories
    }

    public DiscogsInventory(Discogs discogs, IdGenerator idGenerator, Inventories inventories) {
        this.discogs = discogs;
        this.idGenerator = idGenerator;
        this.inventories = inventories;
    }

    @Override
    public String fetch(String user) {
        List<Listing> listings = discogs.inventory(user);
        String inventoryId = idGenerator.generate();
        inventories.save(inventoryId, listings);
        return inventoryId;
    }
}
