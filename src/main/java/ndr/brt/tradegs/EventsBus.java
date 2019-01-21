package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.*;
import java.util.function.Consumer;

public class EventsBus implements Bus {
    private EventBus bus;
    private Map<String, Handlers> handlers = new HashMap<>();

    EventsBus(EventBus bus) {
        this.bus = bus;
        this.bus.consumer("events", (Message<String> message) -> {
            Envelope envelope = Json.fromJson(message.body(), Envelope.class);
            Optional.ofNullable(handlers.get(envelope.type))
                    .ifPresent(handler -> {
                        Object event = Json.fromJson(envelope.json, handler.clazz());
                        handler.accept(event);
                    });
        });
    }

    @Override
    public void publish(Event event) {
        Envelope envelope = new Envelope<>(event);
        bus.publish("events", Json.toJson(envelope));
    }

    @Override
    public <T extends Event> void on(Class<T> clazz, Consumer<T> consumer) {
        Handlers<T> handlers = this.handlers.getOrDefault(clazz.getSimpleName(), new Handlers<>(clazz));
        handlers.add(consumer);
        this.handlers.put(clazz.getSimpleName(), handlers);
    }

}
