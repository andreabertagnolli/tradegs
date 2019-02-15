package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Event;
import ndr.brt.tradegs.wantlist.WantlistFetched;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class User {

    private String id;
    transient private List<Event> changes = new ArrayList<>();
    private String inventoryId;
    private String wantlistId;

    public String id() {
        return id;
    }

    public User created(String id) {
        emit(new UserCreated(id));
        return this;
    }

    public User inventoryFetched(String inventoryId) {
        emit(new InventoryFetched(id, inventoryId));
        return this;
    }

    public User wantlistFetched(String wantlistId) {
        emit(new WantlistFetched(id, wantlistId));
        return this;
    }

    void apply(Event event) {
        switch (event.type()) {
            case "UserCreated":
                apply(UserCreated.class.cast(event));
                break;
            case "InventoryFetched":
                apply(InventoryFetched.class.cast(event));
                break;
            case "WantlistFetched":
                apply(WantlistFetched.class.cast(event));
                break;
            default: throw new RuntimeException("Unknown event type: " + event.getClass().getComponentType());
        }
    }

    private void emit(Event event) {
        apply(event);
        changes.add(event);
    }

    private void apply(UserCreated event) {
        this.id = event.id();
    }

    private void apply(InventoryFetched event) {
        this.inventoryId = event.inventoryId();
    }

    private void apply(WantlistFetched event) {
        this.wantlistId = event.wantlistId();
    }

    public boolean exists() {
        return id != null;
    }

    public Stream<Event> changes() {
        return changes.stream();
    }

    public String inventoryId() {
        return inventoryId;
    }

    public String wantlistId() {
        return wantlistId;
    }

    public void clearChanges() {
        changes.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                '}';
    }
}
