package ndr.brt.tradegs.user;

import ndr.brt.tradegs.inventory.InventoryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class FetchInventoryClientHandlerTest {

    private InventoryClient inventoryClient = mock(InventoryClient.class);
    private Users users = mock(Users.class);
    private FetchInventoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FetchInventoryHandler(inventoryClient, users);
    }

    @Test
    void fetch_inventory() {
        when(inventoryClient.fetch("user")).thenReturn("inventoryId");
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.handle(new FetchInventory("user"));

        verify(users).save(argThat(it -> "inventoryId".equals(it.inventoryId())));
    }
}