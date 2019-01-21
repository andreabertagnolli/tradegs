package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.wantlist.FetchWantlist;

import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class UserCreatedListener implements Consumer<UserCreated> {

    private final Bus commands;

    public UserCreatedListener(Bus commands) {
        this.commands = commands;
    }

    @Override
    public void accept(UserCreated userCreated) {
        String id = userCreated.id();
        asList(new FetchInventory(id), new FetchWantlist(id))
                .forEach(commands::publish);
    }
}
