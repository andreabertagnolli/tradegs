package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.inventory.IdGenerator;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<String> fetch(String user) {
        return discogs.wantlist(user).thenApply(wants -> {
            String inventoryId = idGenerator.generate();
            wantlists.save(inventoryId, wants);
            return inventoryId;
        });
    }
}
