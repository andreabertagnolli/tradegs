package ndr.brt.tradegs;

public interface Commands {

    static Commands commands() {
        return new Instance();
    }

    <T extends Command> void post(Command command);

    class Instance implements Commands {

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Command> void post(Command command) {
            Handler handler = new CreateUserHandler();
            handler.handle(command);
        }
    }
}
