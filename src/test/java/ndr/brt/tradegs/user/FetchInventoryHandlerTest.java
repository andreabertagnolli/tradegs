package ndr.brt.tradegs.user;

import ndr.brt.tradegs.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class FetchInventoryHandlerTest {

    private Inventory inventory = mock(Inventory.class);
    private Users users = mock(Users.class);
    private FetchInventoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FetchInventoryHandler(inventory, users);
    }

    @Test
    void fetch_inventory() {
        when(inventory.fetch("user")).thenReturn("inventoryId");
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.handle(new FetchInventory("user"));

        verify(users).save(argThat(it -> "inventoryId".equals(it.inventoryId())));
    }
}