package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.Handler;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;

public class FetchWantlistHandler implements Handler<FetchWantlist> {
    private final WantlistClient wantlistClient;
    private final Users users;

    public FetchWantlistHandler(WantlistClient wantlistClient, Users users) {
        this.wantlistClient = wantlistClient;
        this.users = users;
    }

    @Override
    public void handle(FetchWantlist command) {
        User user = users.get(command.userId());
        String inventoryId = wantlistClient.fetch(command.userId());

        user.wantlistFetched(inventoryId);

        users.save(user);
    }
}
