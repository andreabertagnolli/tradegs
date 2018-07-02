package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.Command;

public class FetchWantlist extends Command {
    private final String user;

    public FetchWantlist(String user) {
        this.user = user;
    }
}
