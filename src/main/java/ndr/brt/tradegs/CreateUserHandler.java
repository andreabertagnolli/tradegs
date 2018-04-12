package ndr.brt.tradegs;

public class CreateUserHandler implements Handler<CreateUser> {

    private final Users users;

    public CreateUserHandler() {
        this(DbUsers.DbUsers);
    }

    public CreateUserHandler(Users users) {
        this.users = users;
    }

    @Override
    public void handle(CreateUser command) {
        String id = command.id().get();
        User user = users.get(id);

        if (!user.exists()) {
            user.created(id);
            users.save(user);
        }

    }
}
