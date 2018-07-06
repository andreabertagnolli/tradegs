package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;

import java.util.List;

public interface Discogs {
    List<Listing> inventory(String utente);

    List<Want> wantlist(String utente);
}
