package ndr.brt.tradegs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public class ResourceTest {

    protected static final int LISTEN_PORT = 8164;

    static {
        Spark.port(LISTEN_PORT);
    }

    @BeforeEach
    public void sparkInit() {
        Spark.init();
        Spark.awaitInitialization();
    }

    @AfterEach
    public void sparkStop() {
        Spark.stop();
        awaitStop();
    }

    private void awaitStop() {
        try {
            Spark.awaitInitialization();
            Thread.sleep(100);
            awaitStop();
        } catch (Exception e) {

        }
    }

}
