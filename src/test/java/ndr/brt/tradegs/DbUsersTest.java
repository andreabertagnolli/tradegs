package ndr.brt.tradegs;

import com.rabbitmq.client.*;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import ndr.brt.tradegs.user.DbUsers;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.UserCreated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static ndr.brt.tradegs.RabbitConnection.rabbitConnection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class DbUsersTest {

    private MongodProcess mongod;
    private EmbeddedRabbitBroker rabbit;
    private BlockingQueue<String> events = new ArrayBlockingQueue<>(1);
    private DbUsers users;

    @BeforeEach
    void setUp() throws IOException {
        rabbit = new EmbeddedRabbitBroker();
        rabbit.start();

        mongod = MongodStarter.getDefaultInstance()
                .prepare(new MongodConfigBuilder()
                        .version(Version.Main.PRODUCTION)
                        .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                        .build())
                .start();

        users = DbUsers.DbUsers;
        pollEventsTo(events);
    }

    @AfterEach
    void tearDown() {
        mongod.stop();
        rabbit.stop();
    }

    @Test
    void save_and_retrieve_user() throws InterruptedException {
        User user = new User()
                .created("sattad");

        users.save(user);

        assertThat(users.get("sattad"), is(user));
        assertThat(user.changes().count(), is(0L));
        UserCreated event = Json.fromJson(events.poll(5, SECONDS), UserCreated.class);
        assertThat(event.id(), is("sattad"));
    }

    private void pollEventsTo(BlockingQueue<String> queue) {
        try {
            Channel channel = rabbitConnection().createChannel();
            channel.queueDeclare("test", false, false, true, null);
            channel.queueBind("test", "tradegs", "user.created");
            channel.basicConsume("test", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        queue.put(new String(body));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}