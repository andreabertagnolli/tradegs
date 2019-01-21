package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class VertxBus implements Bus {

    private final String address;
    private final EventBus bus;
    private final Map<String, Handlers> handlers = new HashMap<>();

    public VertxBus(String address, EventBus eventBus) {
        this.address = address;
        bus = eventBus;
        bus.consumer(address, (Message<String> message) -> {
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
        bus.publish(address, Json.toJson(envelope));
    }

    @Override
    public <T extends Event> void on(Class<T> clazz, Consumer<T> consumer) {
        Handlers<T> handlers = this.handlers.getOrDefault(clazz.getSimpleName(), new Handlers<>(clazz));
        handlers.add(consumer);
        this.handlers.put(clazz.getSimpleName(), handlers);
    }
}
