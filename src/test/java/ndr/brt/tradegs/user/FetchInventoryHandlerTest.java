package ndr.brt.tradegs.user;

import ndr.brt.tradegs.inventory.Inventory;
import ndr.brt.tradegs.inventory.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(inventory.fetch("user")).thenReturn(asList(new Listing(), new Listing()));
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.handle(new FetchInventory("user"));

        verify(users).save(argThat(User::hasInventoryFetched));
    }
}