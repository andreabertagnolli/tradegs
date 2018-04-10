package ndr.brt.tradegs;

import com.google.gson.Gson;

public enum Json {
    INSTANCE;

    private final Gson gson;

    Json() {
        gson = new Gson();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return INSTANCE.gson.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return INSTANCE.gson.toJson(object);
    }
}
