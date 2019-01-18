package ndr.brt.tradegs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import ndr.brt.tradegs.user.CreateUserApi;
import ndr.brt.tradegs.user.UserCreated;
import ndr.brt.tradegs.user.UserCreatedListener;

import static ndr.brt.tradegs.Commands.commands;

public class TradegsVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        Events events = Events.bus(vertx.eventBus());
        Commands commands = commands(events);

        events.on(UserCreated.class, new UserCreatedListener(commands));

        Router router = Router.router(vertx);
        new CreateUserApi(router, commands).run();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }
}
