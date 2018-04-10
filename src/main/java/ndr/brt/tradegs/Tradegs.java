package ndr.brt.tradegs;

import spark.servlet.SparkApplication;

import static spark.Spark.post;

public class Tradegs implements SparkApplication {

    private final Commands commands;

    public Tradegs(Commands commands) {
        this.commands = commands;
    }

    public void init() {
        post("/users", (req, res) -> {
            CreateUser command = Json.fromJson(req.body(), CreateUser.class);
            commands.post(command);
            return 200;
        });
    }

}
