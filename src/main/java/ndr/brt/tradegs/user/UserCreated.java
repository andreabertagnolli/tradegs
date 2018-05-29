package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Event;

public class UserCreated extends Event {

    private final String id;

    public UserCreated(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
