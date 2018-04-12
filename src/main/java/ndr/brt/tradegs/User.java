package ndr.brt.tradegs;

import java.util.Objects;

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
