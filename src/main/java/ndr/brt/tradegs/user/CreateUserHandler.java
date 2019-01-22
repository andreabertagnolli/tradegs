package ndr.brt.tradegs.user;

import ndr.brt.tradegs.Listener;

public class CreateUserHandler implements Listener<CreateUser> {

    private final Users users;

    public CreateUserHandler(Users users) {
        this.users = users;
    }

    @Override
    public void consume(CreateUser command) {
        String id = command.id().get();
        User user = users.get(id);

        if (!user.exists()) {
            user.created(id);
            users.save(user);
        }

    }
}
