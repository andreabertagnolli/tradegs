package ndr.brt.tradegs.wantlist;

import java.util.concurrent.CompletableFuture;

public interface WantlistClient {
    CompletableFuture<String> fetch(String user);
}
