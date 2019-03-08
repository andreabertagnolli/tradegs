package ndr.brt.tradegs;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class DiscogsClientTest {

    private DiscogsClient client;

    @BeforeEach
    void setUp(Vertx vertx) {
        client = new DiscogsClient(vertx);
    }

    @Disabled
    @Test
    @Timeout(20000)
    void check_inventory_and_handle_pagination(VertxTestContext context) {
        client.inventory("smellymilk").thenAccept(listings -> {
            assertThat(listings.size()).isGreaterThan(50);
            context.completeNow();
        });
    }

    @Disabled
    @Test
    @Timeout(20000)
    void check_wantlist_and_handle_pagination(VertxTestContext context) {
        client.wantlist("smellymilk").thenAccept(wants -> {
            assertThat(wants.size()).isGreaterThan(50);
            context.completeNow();
        });
    }
}