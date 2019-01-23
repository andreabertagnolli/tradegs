package ndr.brt.tradegs.user;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import ndr.brt.tradegs.ApiEndpoint;
import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.Json;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class CreateUserApi implements ApiEndpoint {

    private final Logger log = getLogger(getClass());
    private final Router router;
    private final Bus commands;

    public CreateUserApi(Router router, Bus commands) {
        this.router = router;
        this.commands = commands;
    }

    @Override
    public void run() {
        router.post("/users")
            .handler(context -> {
                Handler<Buffer> bufferHandler = it -> {
                    CreateUser createUser = Json.fromJson(it.toString(), CreateUser.class);
                    if (createUser.id().isPresent()) {
                        log.info("CreateUser request");
                        commands.publish(createUser);
                        context.response().setStatusCode(200).end("User created");
                    } else {
                        context.response().setStatusCode(500).end("Bad request");
                    }
                };
                context.request().bodyHandler(bufferHandler);
            });
    }
}
