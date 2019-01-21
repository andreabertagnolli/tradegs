package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.function.Consumer;

public class VertxBus implements Bus {

    private final String address;
    private final EventBus bus;

    public VertxBus(String address, EventBus eventBus) {
        this.address = address;
        bus = eventBus;
    }

    @Override
    public void publish(Event event) {
        Envelope envelope = new Envelope<>(event);
        bus.publish(address, Json.toJson(envelope));
    }

    @Override
    public <T extends Event> void on(Class<T> clazz, Consumer<T> consumer) {
        bus.consumer(address, (Message<String> message) -> {
            Envelope envelope = Json.fromJson(message.body(), Envelope.class);
            if (clazz.getSimpleName().equals(envelope.type)) {
                Event event = Json.fromJson(envelope.json, clazz);
                consumer.accept((T) event);
            }
        });
    }
}
