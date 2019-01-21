package ndr.brt.tradegs;

import java.util.function.Consumer;

public interface Handler<T extends Command> extends Consumer<T> {
    void handle(T command);

    @Override
    default void accept(T t) {
        handle(t);
    }
}
