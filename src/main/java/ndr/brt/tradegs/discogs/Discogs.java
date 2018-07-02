package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Listing;

import java.util.List;

public interface Discogs {
    List<Listing> inventory(String utente);
}
