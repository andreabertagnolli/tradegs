package ndr.brt.tradegs;

import io.vertx.core.Vertx;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class EventsBusTest {

    @Test
    @Timeout(1000)
    void executed_publisher_based_on_event_type(Vertx vertx, VertxTestContext context) {
        Bus eventsBus = new EventsBus(vertx.eventBus());

        eventsBus.on(WrongEvent.class, event -> context.failNow(new Exception("It should do nothing")));
        eventsBus.on(RightEvent.class, event -> context.completeNow());

        eventsBus.publish(new RightEvent());
    }

    @Test
    @Timeout(1000)
    void handle_more_than_one_consumer(Vertx vertx, VertxTestContext context) {
        Bus eventsBus = new EventsBus(vertx.eventBus());

        Checkpoint checkpoint = context.checkpoint(2);

        eventsBus.on(RightEvent.class, event -> checkpoint.flag());
        eventsBus.on(RightEvent.class, event -> checkpoint.flag());

        eventsBus.publish(new RightEvent());
    }

    private class RightEvent extends Event { }
    private class WrongEvent extends Event { }
}