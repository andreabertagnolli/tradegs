package ndr.brt.tradegs;

public interface Commands {
    static Commands commands() {
        return new Instance();
    }

    void post(Command command);

    class Instance implements Commands {
        @Override
        public void post(Command command) {

        }
    }
}
