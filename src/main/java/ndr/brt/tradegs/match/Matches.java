package ndr.brt.tradegs.match;

import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.inventory.DbInventories;
import ndr.brt.tradegs.user.DbUsers;
import ndr.brt.tradegs.wantlist.DbWantlists;

import java.util.List;

public interface Matches {
    static Matches asRealTime(Bus events) {
        return new RealTimeMatches(new DbUsers(events), new DbWantlists(), new DbInventories());
    }

    List<Match> get(String anUser);
}
