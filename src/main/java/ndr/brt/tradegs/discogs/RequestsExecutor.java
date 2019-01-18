package ndr.brt.tradegs.discogs;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface RequestsExecutor {
    HttpResponse<String> execute(HttpRequest request);
}
