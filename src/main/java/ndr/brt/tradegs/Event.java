package ndr.brt.tradegs;

import java.time.LocalDateTime;

public class Event {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String type = getClass().getSimpleName();

    public String type() {
        return type;
    }
}
