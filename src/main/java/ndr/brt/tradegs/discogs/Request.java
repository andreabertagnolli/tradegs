package ndr.brt.tradegs.discogs;

import io.vertx.core.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {
    private final HttpMethod method;
    private final String url;
    private final String host;
    private final int port;
    private final Map<String, String> headers = new HashMap<>();

    public Request(HttpMethod method, String url) {
        this(method, url, 443, "discogs.com");
    }

    public Request(HttpMethod method, String url, int port, String host) {
        this.method = method;
        this.url = url;
        this.port = port;
        this.host = host;
    }

    public static Request get(String url) {
        return new Request(HttpMethod.GET, url);
    }

    public HttpMethod method() {
        return method;
    }

    public String url() {
        return url;
    }

    public Request header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Set<Map.Entry<String, String>> headers() {
        return headers.entrySet();
    }

    public int port() {
        return 80;
    }

    public String host() {
        return "discogs.com";
    }
}
