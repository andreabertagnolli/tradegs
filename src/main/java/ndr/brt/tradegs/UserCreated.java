package ndr.brt.tradegs;

public class UserCreated extends Event {

    private final String id;

    public UserCreated(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
