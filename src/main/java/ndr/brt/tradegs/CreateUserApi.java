package ndr.brt.tradegs;

import io.vertx.ext.web.Router;
import ndr.brt.tradegs.user.CreateUser;

public class CreateUserApi implements ApiEndpoint {
    private Router router;
    private Commands commands;

    public CreateUserApi(Router router, Commands commands) {
        this.router = router;
        this.commands = commands;
    }

    @Override
    public void run() {
        router.post("/users").handler(context -> context.request().bodyHandler(it -> {
            CreateUser createUser = Json.fromJson(it.toString(), CreateUser.class);
            if (createUser.id().isPresent()) {
                commands.post(createUser);
                context.response().setStatusCode(200).end("User created");
            }
            else {
                context.response().setStatusCode(500).end("Bad request");
            }
        }));
    }
}
