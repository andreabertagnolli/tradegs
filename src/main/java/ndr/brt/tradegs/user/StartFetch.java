package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Command;

public class StartFetch extends Command {
    private final String userId;

    public StartFetch(String userId) {
        this.userId = userId;
    }
}
