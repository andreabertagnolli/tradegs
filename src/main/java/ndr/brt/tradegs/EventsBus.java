package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EventsBus implements Events {
    private EventBus bus;
    private Map<String, Pair<Class, Consumer>> consumers = new HashMap<>();

    EventsBus(EventBus bus) {
        this.bus = bus;
        this.bus.consumer("events", (Message<String> message) -> {
            Envelope envelope = Json.fromJson(message.body(), Envelope.class);
            Optional.ofNullable(consumers.get(envelope.type))
                    .ifPresent(it -> {
                        Object event = Json.fromJson(envelope.event, it.getLeft());
                        it.getRight().accept(event);
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
        consumers.put(clazz.getSimpleName(), Pair.of(clazz, consumer));
    }

    private class Envelope {

        private String event;
        private String type;

        public Envelope(Event event) {
            this.event = Json.toJson(event);
            this.type = event.getClass().getSimpleName();
        }

    }
}
