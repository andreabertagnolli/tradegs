package ndr.brt.tradegs;

import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;

import java.io.IOException;

import static de.flapdoodle.embed.mongo.MongodStarter.getDefaultInstance;
import static org.slf4j.LoggerFactory.getLogger;

public enum EmbeddedMongoDb {

    Instance;

    public static void initialize() {
        LOG.info("Initialize EmbeddedMongoDb");
    }

    private static final Logger LOG = getLogger(EmbeddedMongoDb.class);

    private MongodProcess mongod;
    private final IMongodConfig config;

    EmbeddedMongoDb() {
        try {
            config = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                    .build();
            start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            mongod = getDefaultInstance().prepare(config)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (mongod != null) {
            mongod.stop();
        }
    }

}
