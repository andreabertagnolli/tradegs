package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Command;

import java.util.Optional;

public class CreateUser extends Command {

    private final String id;

    public CreateUser(String id) {
        this.id = id;
    }

    public Optional<String> id() {
        return Optional.ofNullable(id);
    }

}
