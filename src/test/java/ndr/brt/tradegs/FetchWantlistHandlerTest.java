package ndr.brt.tradegs;

import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;
import ndr.brt.tradegs.wantlist.WantlistClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class FetchWantlistHandlerTest {

    private WantlistClient wantlistClient = mock(WantlistClient.class);
    private Users users = mock(Users.class);
    private FetchWantlistHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FetchWantlistHandler(wantlistClient, users);
    }

    @Test
    void fetch_inventory() {
        when(wantlistClient.fetch("user")).thenReturn("wantlistId");
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.handle(new FetchWantlist("user"));

        verify(users).save(argThat(it -> "wantlistId".equals(it.wantlistId())));
    }

}