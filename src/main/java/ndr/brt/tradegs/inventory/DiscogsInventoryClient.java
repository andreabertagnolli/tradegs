package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.Discogs;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<String> fetch(String user) {
        return discogs.inventory(user).thenApply(listings -> {
            String inventoryId = idGenerator.generate();
            inventories.save(inventoryId, listings);
            return inventoryId;
        });
    }
}
