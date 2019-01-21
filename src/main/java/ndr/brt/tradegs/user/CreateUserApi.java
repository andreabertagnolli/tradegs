package ndr.brt.tradegs.user;

import io.vertx.ext.web.Router;
import ndr.brt.tradegs.ApiEndpoint;
import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.Json;

public class CreateUserApi implements ApiEndpoint {
    private Router router;
    private Bus commands;

    public CreateUserApi(Router router, Bus commands) {
        this.router = router;
        this.commands = commands;
    }

    @Override
    public void run() {
        router.post("/users").handler(context -> context.request().bodyHandler(it -> {
            CreateUser createUser = Json.fromJson(it.toString(), CreateUser.class);
            if (createUser.id().isPresent()) {
                commands.publish(createUser);
                context.response().setStatusCode(200).end("User created");
            }
            else {
                context.response().setStatusCode(500).end("Bad request");
            }
        }));
    }
}
