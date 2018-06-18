package ndr.brt.tradegs.inventory;

import java.util.UUID;

public interface IdGenerator {
    static IdGenerator uuidGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    String generate();
}
