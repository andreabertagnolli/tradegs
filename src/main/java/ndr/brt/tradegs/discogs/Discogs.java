package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Discogs {

    CompletableFuture<List<Listing>> inventory(String user);

    CompletableFuture<List<Want>> wantlist(String user);
}
