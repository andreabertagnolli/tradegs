package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;

import java.util.List;

public interface Discogs {

    Future<List<Listing>> inventory(String user);

    Future<List<Want>> wantlist(String user);
}
