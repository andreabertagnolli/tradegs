package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface Requests {

    Future<Buffer> execute(Request request);
}
