package ndr.brt.tradegs;

import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.user.*;

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
                    FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(), DbUsers.DbUsers)
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
