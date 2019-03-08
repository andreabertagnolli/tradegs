package ndr.brt.tradegs.inventory;

import java.util.concurrent.CompletableFuture;

public interface InventoryClient {
    CompletableFuture<String> fetch(String user);
}
