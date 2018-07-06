package ndr.brt.tradegs.discogs;

import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public interface RequestsExecutor {
    HttpResponse<String> execute(HttpRequest request);
}
