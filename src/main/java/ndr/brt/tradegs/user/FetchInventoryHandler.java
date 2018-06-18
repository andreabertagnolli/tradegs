package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Handler;
import ndr.brt.tradegs.inventory.InventoryClient;

public class FetchInventoryHandler implements Handler<FetchInventory> {
    private final InventoryClient inventoryClient;
    private final Users users;

    public FetchInventoryHandler(InventoryClient inventoryClient, Users users) {
        this.inventoryClient = inventoryClient;
        this.users = users;
    }

    @Override
    public void handle(FetchInventory command) {
        User user = users.get(command.userId());
        String inventoryId = inventoryClient.fetch(command.userId());

        user.inventoryFetched(inventoryId);

        users.save(user);
    }
}
