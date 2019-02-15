package ndr.brt.tradegs.integration;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.Bus;
import ndr.brt.tradegs.user.DbUsers;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.UserCreated;
import ndr.brt.tradegs.user.Users;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class DbUsersIT {

    private Users users;
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

        List<User> results = users.stream().collect(toList());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).id()).isEqualTo("sattad");
        context.completeNow();
    }

    @Test
    @Timeout(1000)
    void update_snapshot(VertxTestContext context) {
        User user = new User().created("sattad");

        users.save(user);

        user.inventoryFetched("inventoryId");

        users.save(user);

        List<User> results = users.stream().collect(toList());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).inventoryId()).isEqualTo("inventoryId");
        context.completeNow();
    }

}