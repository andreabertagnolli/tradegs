package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Command;

public class FetchInventory extends Command {
    private final String userId;

    public FetchInventory(String userId) {
        this.userId = userId;
    }

    public String userId() {
        return userId;
    }
}
