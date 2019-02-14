package ndr.brt.tradegs.integration;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.user.DbUsers;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.UserCreated;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class DbUsersIT {

    private DbUsers users;
    private Bus events;

    @BeforeAll
    static void setup() {
        EmbeddedMongoDb.activate();
    }

    @BeforeEach
    void setUp(Vertx vertx) {
        events = Bus.events(vertx.eventBus());
        users = new DbUsers(events);
    }

    @Test
    @Timeout(1000)
    void when_save_user_events_are_published(VertxTestContext context) {
        events.on(UserCreated.class, event -> {
            assertThat(event.id()).isEqualTo("sattad");
            context.completeNow();
        });

        users.save(new User().created("sattad"));
    }

    @Test
    @Timeout(1000)
    void save_and_retrieve_user(VertxTestContext context) {
        User user = new User().created("sattad");

        users.save(user);

        assertThat(users.get("sattad")).isEqualTo(user);
        assertThat(user.changes().count()).isEqualTo(0L);
        context.completeNow();
    }

    @Test
    @Timeout(1000)
    void save_snapshots_and_get_stream(VertxTestContext context) {
        User user = new User().created("sattad");

        users.save(user);

        assertThat(users.stream().count()).isEqualTo(1);
        context.completeNow();
    }

}