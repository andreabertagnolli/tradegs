package ndr.brt.tradegs;

import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.user.*;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;

import java.util.Map;
import java.util.Optional;

public interface Commands {

    static Commands commands() {
        return new Instance();
    }

    <T extends Command> void post(T command);

    class Instance implements Commands {

        private final Map<Class, Handler> handlers;

        private Instance() {
            handlers = Map.of(
                    CreateUser.class, new CreateUserHandler(DbUsers.DbUsers),
                    FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(), DbUsers.DbUsers),
                    FetchWantlist.class, new FetchWantlistHandler(null, DbUsers.DbUsers) // TODO: implement Wantlist client
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Command> void post(T command) {
            Optional.of(command)
                    .map(Object::getClass)
                    .map(handlers::get)
                    .ifPresent(it -> it.handle(command));
        }
    }
}
