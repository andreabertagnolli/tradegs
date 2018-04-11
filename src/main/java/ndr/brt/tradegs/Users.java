package ndr.brt.tradegs;

import java.util.Optional;

public interface Users {
    void save(User user);

    Optional<User> get(String user);
}
