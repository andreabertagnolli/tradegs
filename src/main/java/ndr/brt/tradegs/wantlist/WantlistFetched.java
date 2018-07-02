package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.Event;

public class WantlistFetched extends Event {
    private final String userId;
    private final String wantlistId;

    public WantlistFetched(String userId, String wantlistId) {
        this.userId = userId;
        this.wantlistId = wantlistId;
    }

    public String wantlistId() {
        return wantlistId;
    }
}
