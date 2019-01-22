package ndr.brt.tradegs;

public interface Listener<T> {
    void consume(T object);
}
