package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Command;

import java.util.Objects;
import java.util.Optional;

public class CreateUser implements Command {

    private final String id;

    public CreateUser(String id) {
        this.id = id;
    }

    public Optional<String> id() {
        return Optional.ofNullable(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUser that = (CreateUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
