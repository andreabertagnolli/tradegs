package ndr.brt.tradegs.wantlist;

import io.vertx.core.Future;

public interface WantlistClient {
    Future<String> fetch(String user);
}
