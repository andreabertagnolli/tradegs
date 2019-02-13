package ndr.brt.tradegs;

import io.vertx.core.Future;
import ndr.brt.tradegs.inventory.InventoryClient;
import ndr.brt.tradegs.user.FetchInventory;
import ndr.brt.tradegs.user.FetchInventoryHandler;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class FetchInventoryHandlerTest {

    private InventoryClient inventoryClient = mock(InventoryClient.class);
    private Users users = mock(Users.class);
    private FetchInventoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FetchInventoryHandler(inventoryClient, users);
    }

    @Test
    void fetch_inventory() {
        when(inventoryClient.fetch("user")).thenReturn(Future.succeededFuture("inventoryId"));
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.consume(new FetchInventory("user"));

        verify(users).save(argThat(it -> "inventoryId".equals(it.inventoryId())));
    }
}