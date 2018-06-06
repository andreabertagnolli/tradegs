package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Handler;
import ndr.brt.tradegs.inventory.Inventory;

public class FetchInventoryHandler implements Handler<FetchInventory> {
    private final Inventory inventory;
    private final Users users;

    public FetchInventoryHandler(Inventory inventory, Users users) {
        this.inventory = inventory;
        this.users = users;
    }

    @Override
    public void handle(FetchInventory command) {
        User user = users.get(command.userId());
        String inventoryId = inventory.fetch(command.userId());

        user.inventoryFetched(inventoryId);

        users.save(user);
    }
}
