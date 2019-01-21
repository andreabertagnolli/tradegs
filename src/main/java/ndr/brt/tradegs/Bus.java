package ndr.brt.tradegs;

import io.vertx.core.eventbus.EventBus;
import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.user.*;
import ndr.brt.tradegs.wantlist.DiscogsWantlistClient;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static ndr.brt.tradegs.inventory.Inventories.inventories;
import static ndr.brt.tradegs.wantlist.Wantlists.wantlists;

public interface Bus {

    static Bus events(EventBus bus) {
        return new VertxBus("events", bus);
    }

    static Bus commands(EventBus eventBus, Bus events) {
        DiscogsClient discogsClient = new DiscogsClient();
        IdGenerator idGenerator = () -> UUID.randomUUID().toString();
        DbUsers dbUsers = new DbUsers(events);

        Bus bus = new VertxBus("commands", eventBus);

        bus.on(CreateUser.class, new CreateUserHandler(dbUsers));
        bus.on(FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(discogsClient, idGenerator, inventories()), dbUsers));
        bus.on(FetchWantlist.class, new FetchWantlistHandler(new DiscogsWantlistClient(discogsClient, idGenerator, wantlists()), dbUsers));

        return bus;
    }

    <T extends Object> void publish(T object);

    <T extends Object> void on(Class<T> clazz, Consumer<T> consumer);

    class Envelope<T> {

        final String json;
        final String type;

        Envelope(T object) {
            this.json = Json.toJson(object);
            this.type = object.getClass().getSimpleName();
        }

    }
}
