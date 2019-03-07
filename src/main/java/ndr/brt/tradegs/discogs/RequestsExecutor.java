package ndr.brt.tradegs.discogs;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface RequestsExecutor {

    CompletableFuture<HttpResponse<String>> execute(HttpRequest request);
}
