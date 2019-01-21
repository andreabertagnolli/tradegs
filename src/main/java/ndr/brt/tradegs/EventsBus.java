package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;

class EventsBus extends VertxBus {

    EventsBus(EventBus bus) {
        super("events", bus);
    }

}
