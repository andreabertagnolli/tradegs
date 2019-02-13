package ndr.brt.tradegs;

import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;
import ndr.brt.tradegs.wantlist.WantlistClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
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
        when(wantlistClient.fetch("user")).thenReturn(Future.succeededFuture("wantlistId"));
        when(users.get("user")).thenReturn(new User().created("user"));

        handler.consume(new FetchWantlist("user"));

        verify(users).save(argThat(it -> "wantlistId".equals(it.wantlistId())));
    }

}