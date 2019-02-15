package ndr.brt.tradegs.user;

import java.util.stream.Stream;

public interface Users {
    void save(User user);

    User get(String user);

    Stream<User> stream();
}
