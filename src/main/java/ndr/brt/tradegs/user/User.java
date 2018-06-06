package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Event;
import ndr.brt.tradegs.inventory.Listing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class User {

    private String id;
    private List<Event> changes = new ArrayList<>();
    private String inventoryId;

    public String id() {
        return id;
    }

    public User created(String id) {
        UserCreated event = new UserCreated(id);

        apply(event);
        changes.add(event);
        return this;
    }

    public void inventoryFetched(String inventoryId) {
        InventoryFetched event = new InventoryFetched(id, inventoryId);

        apply(event);
        changes.add(event);
    }

    void apply(Event event) {
        switch (event.type()) {
            case "UserCreated":
                apply(UserCreated.class.cast(event));
                break;
            case "InventoryFetched":
                apply(InventoryFetched.class.cast(event));
                break;
            default: throw new RuntimeException("Unknown event type: " + event.getClass().getComponentType());
        }
    }

    private void apply(UserCreated event) {
        this.id = event.id();
    }

    private void apply(InventoryFetched event) {
        this.inventoryId = event.inventoryId();
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
