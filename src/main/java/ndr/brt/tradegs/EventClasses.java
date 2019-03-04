package ndr.brt.tradegs;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum EventClasses {
    INSTANCE;

    private ConcurrentMap<String, Class<? extends Event>> map = new ConcurrentHashMap<>();

    EventClasses() {
        Reflections reflections = new Reflections("ndr.brt");
        reflections.getSubTypesOf(Event.class).forEach(it -> map.put(it.getSimpleName(), it));
    }

    public static Class<? extends Event> get(String className) {
        return INSTANCE.map.get(className);
    }
}
