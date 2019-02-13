package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface RequestsExecutor {

    Future<HttpResponse<String>> execute(HttpRequest request);
}
