package ndr.brt.tradegs;

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

    private MongodProcess mongod;
    //private final IMongodConfig config;

    EmbeddedMongoDb() {
        /*try {
            config = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(host(), port(), localhostIsIPv6()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    public void start() {
        try {

            IMongodConfig config = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(host(), port(), localhostIsIPv6()))
                    .build();
            MongodStarter instance = MongodStarter.getDefaultInstance();
            mongod = instance.prepare(config).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            if (mongod != null) {
                mongod.stop();
            }
        } catch (Throwable ignored) {

        }
    }

}
