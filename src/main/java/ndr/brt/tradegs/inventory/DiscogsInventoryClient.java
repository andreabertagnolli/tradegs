package ndr.brt.tradegs.inventory;

import io.vertx.core.Future;
import ndr.brt.tradegs.discogs.Discogs;

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
    public Future<String> fetch(String user) {
        Future<String> future = Future.future();

        discogs.inventory(user).thenAccept(listings -> {
            String inventoryId = idGenerator.generate();
            inventories.save(inventoryId, listings);
            future.complete(inventoryId);
        }).exceptionally(throwable -> {
            future.fail(future.cause());
            return null;
        });

        return future;
    }
}
