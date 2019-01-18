package ndr.brt.tradegs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import ndr.brt.tradegs.user.CreateUserApi;

import static ndr.brt.tradegs.Commands.commands;

public class TradegsVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);

        new CreateUserApi(router, commands()).run();

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }
}
