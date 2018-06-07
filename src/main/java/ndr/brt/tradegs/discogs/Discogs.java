package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.inventory.Listing;

import java.util.List;

public interface Discogs {
    List<Listing> inventory(String utente);
}
