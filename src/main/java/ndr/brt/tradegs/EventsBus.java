package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.*;
import java.util.function.Consumer;

public class EventsBus implements Bus {
    private EventBus bus;
    private Map<String, Consumers> consumers = new HashMap<>();

    EventsBus(EventBus bus) {
        this.bus = bus;
        this.bus.consumer("events", (Message<String> message) -> {
            Envelope envelope = Json.fromJson(message.body(), Envelope.class);
            Optional.ofNullable(consumers.get(envelope.type))
                    .ifPresent(it -> {
                        Object event = Json.fromJson(envelope.event, it.clazz());
                        it.accept(event);
                    });
        });
    }

    @Override
    public void publish(Event event) {
        Envelope envelope = new Envelope(event);
        bus.publish("events", Json.toJson(envelope));
    }

    @Override
    public <T extends Event> void on(Class<T> clazz, Consumer<T> consumer) {
        Consumers consumers = this.consumers.getOrDefault(clazz.getSimpleName(), new Consumers(clazz));
        consumers.add(consumer);
        this.consumers.put(clazz.getSimpleName(), consumers);
    }

    private class Envelope {

        private String event;
        private String type;

        Envelope(Event event) {
            this.event = Json.toJson(event);
            this.type = event.getClass().getSimpleName();
        }

    }

    private class Consumers {

        private final Class<? extends Event> clazz;
        private final List<Consumer> consumers;

        public <T extends Event> Consumers(Class<T> clazz) {
            this.clazz = clazz;
            this.consumers = new ArrayList<>();
        }

        public <T extends Event> void add(Consumer<T> consumer) {
            consumers.add(consumer);
        }

        public Class clazz() {
            return clazz;
        }

        public void accept(Object event) {
            consumers.forEach(it -> it.accept(event));
        }
    }
}
