package ndr.brt.tradegs;

import ndr.brt.tradegs.user.CreateUserHandler;

public interface Commands {

    static Commands commands() {
        return new Instance();
    }

    <T extends Command> void post(Command command);

    class Instance implements Commands {

        private Handler handler;

        public Instance() {
            handler = new CreateUserHandler();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Command> void post(Command command) {
            handler.handle(command);
        }
    }
}
