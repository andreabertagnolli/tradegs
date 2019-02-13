package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.Listener;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;

public class FetchWantlistHandler implements Listener<FetchWantlist> {
    private final WantlistClient wantlistClient;
    private final Users users;

    public FetchWantlistHandler(WantlistClient wantlistClient, Users users) {
        this.wantlistClient = wantlistClient;
        this.users = users;
    }

    @Override
    public void consume(FetchWantlist command) {
        wantlistClient.fetch(command.userId()).setHandler(async -> {
            if (async.succeeded()) {
                User user = users.get(command.userId());
                user.wantlistFetched(async.result());
                users.save(user);
            } else {
                // TODO: No wantlist, so command should fail? notify error?
            }
        });
    }
}
