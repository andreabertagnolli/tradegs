package ndr.brt.tradegs;

import java.util.Optional;

public enum DbUsers implements Users {

    DbUsers;

    @Override
    public void save(User user) {

    }

    @Override
    public User get(String user) {
        return new User();
    }
}
