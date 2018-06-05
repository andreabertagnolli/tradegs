package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Handler;
import ndr.brt.tradegs.inventory.Inventory;
import ndr.brt.tradegs.inventory.Listing;

import java.util.List;

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
        List<Listing> listings = inventory.fetch(command.userId());
        // TODO: inventory.fetch potrebbe arrangiarsi a salvare la fetchata ed a restituire l'id per riprendere poi tale fetchata

        user.inventoryFetched(listings);

        users.save(user);
    }
}
