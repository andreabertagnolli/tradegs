package ndr.brt.tradegs;

import java.util.Objects;

public class CreateUser implements Command {

    private final String id;

    public CreateUser(String id) {
        this.id = id;
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
