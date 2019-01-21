package ndr.brt.tradegs;

import io.vertx.core.Vertx;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class VertxBusTest {

    private Bus bus;

    @BeforeEach
    void setUp(Vertx vertx) {
        bus = new VertxBus("address", vertx.eventBus());
    }

    @Test
    @Timeout(1000)
    void executed_publisher_based_on_object_type(VertxTestContext context) {
        bus.on(WrongEvent.class, event -> context.failNow(new Exception("It should do nothing")));
        bus.on(RightEvent.class, event -> context.completeNow());

        bus.publish(new RightEvent());
    }

    @Test
    @Timeout(1000)
    void handle_more_than_one_consumer(VertxTestContext context) {
        Checkpoint checkpoint = context.checkpoint(2);

        bus.on(RightEvent.class, event -> checkpoint.flag());
        bus.on(RightEvent.class, event -> checkpoint.flag());

        bus.publish(new RightEvent());
    }

    private class RightEvent extends Event { }
    private class WrongEvent extends Event { }
}