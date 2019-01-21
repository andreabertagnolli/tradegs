package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;

import java.util.function.Consumer;

public interface Bus {

    static Bus bus(EventBus bus) {
        return new EventsBus(bus);
    }

    void publish(Event event);

    <T extends Event> void on(Class<T> clazz, Consumer<T> event);
}
