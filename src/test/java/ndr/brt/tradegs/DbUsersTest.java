package ndr.brt.tradegs;

import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class DbUsersTest {

    private MongodProcess mongod;

    @BeforeEach
    void setUp() throws IOException {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build();

        mongod = MongodStarter.getDefaultInstance()
                .prepare(mongodConfig)
                .start();
    }

    @AfterEach
    void tearDown() {
        mongod.stop();
    }

    @Test
    void save_and_retrieve_user() {
        User user = new User();
        user.created("sattad");

        DbUsers users = DbUsers.DbUsers;

        users.save(user);

        assertThat(users.get("sattad"), is(user));
    }
}