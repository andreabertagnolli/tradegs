package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.inventory.IdGenerator;

import java.util.List;

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
    public String fetch(String user) {
        List<Want> wants = discogs.wantlist(user);
        String wantlistId = idGenerator.generate();
        wantlists.save(wantlistId, wants);
        return wantlistId;
    }
}
