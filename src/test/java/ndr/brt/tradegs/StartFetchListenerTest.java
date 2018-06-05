package ndr.brt.tradegs;

import com.rabbitmq.client.Channel;
import ndr.brt.tradegs.user.StartFetch;
import ndr.brt.tradegs.user.UserCreated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.rabbitmq.client.BuiltinExchangeType.TOPIC;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ndr.brt.tradegs.Json.toJson;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StartFetchListenerTest {

    private EmbeddedRabbitBroker rabbit;
    private Commands commands = mock(Commands.class);
    private StartFetchListener listener;

    @BeforeEach
    void setUp() {
        rabbit = new EmbeddedRabbitBroker();
        rabbit.start();
        listener = new StartFetchListener(commands);
    }

    @AfterEach
    void tearDown() {
        rabbit.stop();
    }

    @Test
    void launch_start_fetch_command() {
        Events.events().publish(toJson(new UserCreated("user")));

        listener.run();

        await().atMost(2, SECONDS)
                .untilAsserted(() -> verify(commands).post(new StartFetch("user")));
    }
}