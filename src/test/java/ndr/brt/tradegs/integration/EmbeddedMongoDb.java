package ndr.brt.tradegs.integration;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import ndr.brt.tradegs.Json;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
                    .cmdOptions(new MongoCmdOptionsBuilder().build())
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
            createUser("test", "test", List.of("readWrite"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createUser(String name, String password, List<String> roles) {
        MongoClient mongo = new MongoClient(host(), port());

        mongo.getDatabase("test")
            .runCommand(
                new BasicDBObject("createUser", name)
                    .append("pwd", password)
                    .append("roles", roles)
            );

        mongo.close();
    }

}
