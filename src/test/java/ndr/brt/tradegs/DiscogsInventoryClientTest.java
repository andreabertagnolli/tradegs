package ndr.brt.tradegs;

import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Release;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.inventory.Inventories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class DiscogsInventoryClientTest {

    private final Discogs discogs = mock(Discogs.class);
    private final Inventories inventories = mock(Inventories.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);

    @Test
    void fetch_listings_from_discogs_persist_it_and_return_the_key(VertxTestContext context) {
        List<Listing> listings = asList(
                new Listing(12, new Release(4321)), new Listing(13, new Release(6432))
        );
        when(discogs.inventory("utente")).thenReturn(Future.succeededFuture(listings));
        when(idGenerator.generate()).thenReturn("idInventory");

        DiscogsInventoryClient inventory = new DiscogsInventoryClient(discogs, idGenerator, inventories);

        inventory.fetch("utente").setHandler(async -> {
            assertThat(async.result(), is("idInventory"));
            verify(inventories).save("utente", listings);
            context.completeNow();
        });

    }
}