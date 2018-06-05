package ndr.brt.tradegs.inventory;

import java.util.List;

public interface Inventory {
    List<Listing> fetch(String user);
}
