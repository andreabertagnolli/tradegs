package ndr.brt.tradegs;

import ndr.brt.tradegs.inventory.DiscogsClient;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.inventory.Inventories;
import ndr.brt.tradegs.user.*;
import ndr.brt.tradegs.wantlist.DiscogsWantlistClient;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;
import ndr.brt.tradegs.wantlist.Wantlists;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Commands {

    static Commands commands() {
        return new Instance();
    }

    <T extends Command> void post(T command);

    class Instance implements Commands {

        private final Map<Class, Handler> handlers;

        private Instance() {

            DiscogsClient discogsClient = new DiscogsClient();
            IdGenerator idGenerator = () -> UUID.randomUUID().toString();
            Inventories inventories = Inventories.inventories();
            Wantlists wantlists = Wantlists.wantlists();

            handlers = Map.of(
                    CreateUser.class, new CreateUserHandler(DbUsers.DbUsers),
                    FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(discogsClient, idGenerator, inventories), DbUsers.DbUsers),
                    FetchWantlist.class, new FetchWantlistHandler(new DiscogsWantlistClient(discogsClient, idGenerator, wantlists), DbUsers.DbUsers) // TODO: implement Wantlist client
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
