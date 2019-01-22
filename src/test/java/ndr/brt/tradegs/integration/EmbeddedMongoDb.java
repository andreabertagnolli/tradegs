package ndr.brt.tradegs.integration;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;

import java.io.IOException;

import static de.flapdoodle.embed.mongo.MongodStarter.getDefaultInstance;
import static de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6;
import static ndr.brt.tradegs.MongoDbConnection.host;
import static ndr.brt.tradegs.MongoDbConnection.port;
import static org.slf4j.LoggerFactory.getLogger;

public class EmbeddedMongoDb {

    private final MongodExecutable executable;

    public static void activate() {
        Instance.INSTANCE.activate();
    }

    enum Instance {
        INSTANCE;

        private EmbeddedMongoDb mongoDb;

        Instance() {
            mongoDb = new EmbeddedMongoDb();
            mongoDb.start();
        }

        public void activate() {
            // I'm already active!
        }
    }

    private EmbeddedMongoDb() {
        IMongodConfig config;
        try {
            config = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(host(), port(), localhostIsIPv6()))
                    .build();
            MongodStarter instance = MongodStarter.getDefaultInstance();
            executable = instance.prepare(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() {
        try {
            executable.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
