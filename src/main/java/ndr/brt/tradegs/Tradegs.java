package ndr.brt.tradegs;

import spark.servlet.SparkApplication;

import static spark.Spark.post;

public class Tradegs implements SparkApplication {

    private final Commands commands;

    @SuppressWarnings("unused")
    public Tradegs() {
        this(Commands.commands());
    }

    public Tradegs(Commands commands) {
        this.commands = commands;
    }

    public void init() {
        post("/users", (req, res) -> {
            CreateUser command = Json.fromJson(req.body(), CreateUser.class);
            if (command.id().isPresent()) {
                commands.post(command);
                return command.id().get();
            }
            else {
                res.status(500);
                return "User id not specified";
            }
        });
    }

}
