package ndr.brt.tradegs;

import ndr.brt.tradegs.user.FetchInventory;
import ndr.brt.tradegs.user.UserCreated;
import ndr.brt.tradegs.user.UserCreatedListener;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserCreatedListenerTest {

    private Bus commands = mock(Bus.class);
    private UserCreatedListener listener;

    @BeforeEach
    void setUp() {
        listener = new UserCreatedListener(commands);
    }

    @Test
    void launch_fetch_inventory_command() {
        listener.accept(new UserCreated("user"));

        verify(commands).publish(new FetchInventory("user"));
        verify(commands).publish(new FetchWantlist("user"));

    }
}