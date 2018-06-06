package ndr.brt.tradegs;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum EventClasses {
    INSTANCE;

    public static void put(String className, Class<? extends Event> clazz) {
        INSTANCE.map.put(className, clazz);
    }

    private ConcurrentMap<String, Class<? extends Event>> map = new ConcurrentHashMap<>();

    public static Class<? extends Event> get(String className) {
        return INSTANCE.map.get(className);
    }
}
