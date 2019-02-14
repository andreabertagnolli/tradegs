package ndr.brt.tradegs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.Router;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.match.Matches;
import ndr.brt.tradegs.user.*;
import ndr.brt.tradegs.wantlist.DiscogsWantlistClient;
import ndr.brt.tradegs.wantlist.FetchWantlist;
import ndr.brt.tradegs.wantlist.FetchWantlistHandler;
import org.slf4j.Logger;

import java.util.UUID;

import static ndr.brt.tradegs.Bus.events;
import static ndr.brt.tradegs.Bus.commands;
import static ndr.brt.tradegs.inventory.Inventories.inventories;
import static ndr.brt.tradegs.wantlist.Wantlists.wantlists;
import static org.slf4j.LoggerFactory.getLogger;

public class TradegsVerticle extends AbstractVerticle {

    private final Logger log = getLogger(getClass());
    private final int httpServerPort;
    private final Discogs discogs;

    public TradegsVerticle(int httpServerPort, Discogs discogs) {
        this.httpServerPort = httpServerPort;
        this.discogs = discogs;
    }

    @Override
    public void start(Future<Void> startFuture) {
        Bus events = events(vertx.eventBus());
        Bus commands = commands(vertx.eventBus());

        IdGenerator idGenerator = () -> UUID.randomUUID().toString();
        DbUsers dbUsers = new DbUsers(events);

        commands.on(CreateUser.class, new CreateUserHandler(dbUsers));
        commands.on(FetchInventory.class, new FetchInventoryHandler(new DiscogsInventoryClient(discogs, idGenerator, inventories()), dbUsers));
        commands.on(FetchWantlist.class, new FetchWantlistHandler(new DiscogsWantlistClient(discogs, idGenerator, wantlists()), dbUsers));

        events.on(UserCreated.class, new UserCreatedListener(commands));
        Matches matches = Matches.asRealTime(events);

        Router router = Router.router(vertx);
        new CreateUserApi(router, commands).run();
        new GetMatchesApi(router, matches).run();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(httpServerPort, async -> {
                    if (async.succeeded()) {
                        log.info("TradegsVerticle started");
                        startFuture.complete();
                    } else {
                        log.error("TradegsVerticle start failed", async.cause());
                        startFuture.fail(async.cause());
                    }
                });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        int httpServerPort = 8080;

        vertx.deployVerticle(new TradegsVerticle(httpServerPort, new DiscogsClient(vertx)), async -> {
            if (async.failed()) {
                async.cause().printStackTrace();
            }
        });
    }
}
