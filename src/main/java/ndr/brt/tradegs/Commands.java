package ndr.brt.tradegs;

import ndr.brt.tradegs.user.CreateUser;
import ndr.brt.tradegs.user.CreateUserHandler;
import ndr.brt.tradegs.user.FetchInventory;
import ndr.brt.tradegs.user.FetchInventoryHandler;

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
                    CreateUser.class, new CreateUserHandler(),
                    FetchInventory.class, new FetchInventoryHandler()
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
