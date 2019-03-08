package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Listener;
import ndr.brt.tradegs.inventory.InventoryClient;

public class FetchInventoryHandler implements Listener<FetchInventory> {
    private final InventoryClient inventoryClient;
    private final Users users;

    public FetchInventoryHandler(InventoryClient inventoryClient, Users users) {
        this.inventoryClient = inventoryClient;
        this.users = users;
    }

    @Override
    public void consume(FetchInventory command) {
        inventoryClient.fetch(command.userId()).thenAccept(inventoryId -> {
            User user = users.get(command.userId());
            user.inventoryFetched(inventoryId);
            users.save(user);
        });
    }
}
