package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface Bus {

    static Bus bus(EventBus bus) {
        return new EventsBus(bus);
    }

    <T extends Event> void publish(T event);

    <T extends Event> void on(Class<T> clazz, Consumer<T> consumer);

    class Handlers<T> {

        private final Class clazz;
        private final List<Consumer> consumers;

        Handlers(Class<T> clazz) {
            this.clazz = clazz;
            this.consumers = new ArrayList<>();
        }

        void add(Consumer<T> consumer) {
            consumers.add(consumer);
        }

        Class clazz() {
            return clazz;
        }

        void accept(Object event) {
            consumers.forEach(it -> it.accept(event));
        }
    }

    class Envelope<T> {

        final String json;
        final String type;

        Envelope(T object) {
            this.json = Json.toJson(object);
            this.type = object.getClass().getSimpleName();
        }

    }
}
