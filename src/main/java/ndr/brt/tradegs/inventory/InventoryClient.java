package ndr.brt.tradegs.inventory;

import io.vertx.core.Future;

public interface InventoryClient {
    Future<String> fetch(String user);
}
