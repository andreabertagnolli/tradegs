package ndr.brt.tradegs;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Event {

    private static Map<String, Class<? extends Event>> eventClasses = new HashMap<>();

    public static Class<? extends Event> classOf(String className) {
        return eventClasses.get(className);
    }

    public Event() {
        eventClasses.put(getClass().getSimpleName(), getClass());
    }

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String type = getClass().getSimpleName();

    public String type() {
        return type;
    }
}
