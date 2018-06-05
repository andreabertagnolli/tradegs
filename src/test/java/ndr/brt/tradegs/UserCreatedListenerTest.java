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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserCreatedListenerTest {

    private EmbeddedRabbitBroker rabbit;
    private Commands commands = mock(Commands.class);
    private UserCreatedListener listener;

    @BeforeEach
    void setUp() {
        rabbit = new EmbeddedRabbitBroker();
        rabbit.start();
        listener = new UserCreatedListener(commands);
    }

    @AfterEach
    void tearDown() {
        rabbit.stop();
    }

    @Test
    void launch_fetch_inventory_command() {
        Events.events().publish(toJson(new UserCreated("user")));

        listener.run();

        await().atMost(2, SECONDS)
                .untilAsserted(() -> verify(commands).post(new FetchInventory("user")));
    }
}