package ndr.brt.tradegs.integration;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.Events;
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
    private Events events;

    @BeforeAll
    static void setup() {
        EmbeddedMongoDb.activate();
    }

    @BeforeEach
    void setUp(Vertx vertx) {
        events = Events.bus(vertx.eventBus());
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
}