package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Commands;
import ndr.brt.tradegs.Events;
import ndr.brt.tradegs.wantlist.FetchWantlist;

import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class UserCreatedListener implements Consumer<UserCreated> {

    private final Commands commands;

    public UserCreatedListener(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void accept(UserCreated userCreated) {
        String id = userCreated.id();
        asList(new FetchInventory(id), new FetchWantlist(id))
                .forEach(commands::post);
    }
}
