package ndr.brt.tradegs;

import java.time.LocalDateTime;

public class Event {

    public Event() {
        EventClasses.put(getClass().getSimpleName(), getClass());
    }

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String type = getClass().getSimpleName();

    public String type() {
        return type;
    }
}
