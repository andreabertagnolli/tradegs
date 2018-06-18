package ndr.brt.tradegs;

import ndr.brt.tradegs.user.CreateUserResource;
import ndr.brt.tradegs.user.UserCreatedListener;
import spark.servlet.SparkApplication;

import static ndr.brt.tradegs.Commands.commands;

public class Tradegs implements SparkApplication {

    private final Commands commands;

    public Tradegs() {
        this(commands());
    }

    public Tradegs(Commands commands) {
        this.commands = commands;
    }

    public void init() {
        new CreateUserResource(commands).run();
        new UserCreatedListener(commands).run();
    }

}
