package ndr.brt.tradegs.user;

import java.util.List;

public interface Users {
    void save(User user);

    User get(String user);

    List<String> except(String user);
}
