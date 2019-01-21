package ndr.brt.tradegs;

import java.util.function.Consumer;

public interface Commands {

    static Commands commands(Bus events) {
        return new CommandsBus(events);
    }

    <T extends Command> void post(T command);

    <T extends Command> void on(Class<T> clazz, Consumer<T> event);

}
