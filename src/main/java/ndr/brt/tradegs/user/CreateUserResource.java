package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Commands;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.Resource;
import spark.Request;
import spark.Response;

import static spark.Spark.post;

public class CreateUserResource implements Resource {

    private final Commands commands;

    public CreateUserResource(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void run() {
        post("users", this);
    }

    @Override
    public Object handle(Request request, Response response) {
        CreateUser command = Json.fromJson(request.body(), CreateUser.class);
        if (command.id().isPresent()) {
            commands.post(command);
            return command.id().get();
        } else {
            response.status(500);
            return "User id not specified";
        }
    }
}
