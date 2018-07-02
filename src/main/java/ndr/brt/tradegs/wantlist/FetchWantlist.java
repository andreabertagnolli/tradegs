package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.Command;

public class FetchWantlist extends Command {
    private final String userId;

    public FetchWantlist(String userId) {
        this.userId = userId;
    }

    public String userId() {
        return userId;
    }
}
