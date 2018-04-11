package ndr.brt.tradegs;

public interface Handler<T extends Command> {
    void handle(T command);
}
