package ndr.brt.tradegs;

import ndr.brt.tradegs.user.FetchInventory;
import ndr.brt.tradegs.user.UserCreated;
import ndr.brt.tradegs.user.UserCreatedListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static ndr.brt.tradegs.Json.toJson;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserCreatedListenerTest {

    private Commands commands = mock(Commands.class);
    private UserCreatedListener listener;

    @BeforeEach
    void setUp() {
        EmbeddedRabbitBroker.initialize();
        EmbeddedMongoDb.initialize();
        listener = new UserCreatedListener(commands);
    }

    @Test
    void launch_fetch_inventory_command() {
        Events.events().publish(toJson(new UserCreated("user")));

        listener.run();

        await().atMost(3, SECONDS)
                .untilAsserted(() -> verify(commands).post(new FetchInventory("user")));
    }
}