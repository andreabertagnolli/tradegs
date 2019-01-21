package ndr.brt.tradegs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.user.*;
import ndr.brt.tradegs.wantlist.DiscogsWantlistClient;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;

import java.util.UUID;

import static ndr.brt.tradegs.Bus.events;
import static ndr.brt.tradegs.Bus.commands;
import static ndr.brt.tradegs.inventory.Inventories.inventories;
import static ndr.brt.tradegs.wantlist.Wantlists.wantlists;

public class TradegsVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        Bus events = events(vertx.eventBus());
        Bus commands = commands(vertx.eventBus());

        DiscogsClient discogsClient = new DiscogsClient();
        IdGenerator idGenerator = () -> UUID.randomUUID().toString();
        DbUsers dbUsers = new DbUsers(events);

        commands.on(CreateUser.class, new CreateUserHandler(dbUsers));
        commands.on(FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(discogsClient, idGenerator, inventories()), dbUsers));
        commands.on(FetchWantlist.class, new FetchWantlistHandler(new DiscogsWantlistClient(discogsClient, idGenerator, wantlists()), dbUsers));

        events.on(UserCreated.class, new UserCreatedListener(commands));

        Router router = Router.router(vertx);
        new CreateUserApi(router, commands).run();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }
}
