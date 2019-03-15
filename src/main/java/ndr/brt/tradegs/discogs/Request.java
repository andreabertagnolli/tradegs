package ndr.brt.tradegs.discogs;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;

public class Request {
    private final HttpMethod method;
    private final RequestOptions options;

    public Request(HttpMethod method, RequestOptions options) {
        this.method = method;
        this.options = options;
    }

    public static Request get(RequestOptions options) {
        return new Request(HttpMethod.GET, options);
    }

    public HttpMethod method() {
        return method;
    }

    public RequestOptions options() {
        return options;
    }
}
