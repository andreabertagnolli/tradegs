package ndr.brt.tradegs.wantlist;

import io.vertx.core.Future;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.inventory.IdGenerator;

public class DiscogsWantlistClient implements WantlistClient {
    private final Discogs discogs;
    private final IdGenerator idGenerator;
    private final Wantlists wantlists;

    public DiscogsWantlistClient(Discogs discogs, IdGenerator idGenerator, Wantlists wantlists) {
        this.discogs = discogs;
        this.idGenerator = idGenerator;
        this.wantlists = wantlists;
    }

    @Override
    public Future<String> fetch(String user) {
        Future<String> future = Future.future();
        discogs.wantlist(user).setHandler(async -> {
            if (async.succeeded()) {
                String inventoryId = idGenerator.generate();
                wantlists.save(inventoryId, async.result());
                future.complete(inventoryId);
            } else {
                future.fail(future.cause());
            }
        });

        return future;
    }
}
