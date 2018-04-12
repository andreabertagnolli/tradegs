package ndr.brt.tradegs;

import java.util.Optional;

public class User {

    private String id;

    public String id() {
        return id;
    }

    public void created(String id) {
        UserCreated event = new UserCreated(id);

        apply(event);
    }

    private void apply(UserCreated event) {
        this.id = event.id();
    }

    public boolean exists() {
        return id != null;
    }
}
