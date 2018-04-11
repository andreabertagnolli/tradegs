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
        users.get(command.id().get())
                .ifPresentOrElse(
                        it -> {},
                        () -> users.save(new User())
                );

    }
}
