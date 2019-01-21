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
    public void publish(Object object) {
        Envelope envelope = new Envelope<>(object);
        bus.publish(address, Json.toJson(envelope));
    }

    @Override
    public void on(Class clazz, Consumer consumer) {
        bus.consumer(address, (Message<String> message) -> {
            Envelope envelope = Json.fromJson(message.body(), Envelope.class);
            if (clazz.getSimpleName().equals(envelope.type)) {
                Object object = Json.fromJson(envelope.json, clazz);
                consumer.accept(object);
            }
        });
    }
}
